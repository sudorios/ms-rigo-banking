package com.bank.creditservice.service.impl;

import com.bank.creditservice.model.Credit;
import com.bank.creditservice.repository.CreditRepository;
import com.bank.creditservice.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.bank.creditservice.util.Constants;
import com.bank.creditservice.util.GenericUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository repository;
    private final WebClient.Builder webClientBuilder;

    @Transactional(readOnly = true)
    @Override
    public Flux<Credit> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Credit> findById(String id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<Credit> findByCustomerId(String customerId) {
        return repository.findAllByCustomerId(customerId);
    }

    @Transactional
    @Override
    public Mono<Credit> save(Credit entity) {
        return webClientBuilder.build().get().uri(Constants.CUSTOMER_API_URL + entity.getCustomerId()).retrieve().bodyToMono(CustomerDto.class).switchIfEmpty(Mono.error(new RuntimeException(Constants.ERROR_CUSTOMER_NOT_FOUND))).flatMap(customer -> repository.findAllByCustomerId(customer.getId()).collectList().flatMap(credits -> {
            boolean hasOverdue = credits.stream().anyMatch(c -> GenericUtil.isNotNull(c.getHasOverdueDebt()) && c.getHasOverdueDebt());
            if (hasOverdue) {
                return Mono.error(new RuntimeException(Constants.ERROR_CUSTOMER_HAS_OVERDUE_DEBT));
            }
            if (Constants.CUSTOMER_TYPE_PERSONAL.equalsIgnoreCase(customer.getCustomerType())) {
                long personalCredits = credits.stream().filter(c -> Constants.CREDIT_TYPE_PERSONAL.equalsIgnoreCase(c.getCreditType())).count();
                if (Constants.CREDIT_TYPE_PERSONAL.equalsIgnoreCase(entity.getCreditType()) && personalCredits >= 1) {
                    return Mono.error(new RuntimeException(Constants.ERROR_PERSONAL_CUSTOMER_ONE_CREDIT));
                }
            }
            entity.setCreatedAt(LocalDateTime.now());
            if (GenericUtil.isNull(entity.getAvailableBalance())) entity.setAvailableBalance(entity.getLimitAmount());
            if (GenericUtil.isNull(entity.getCurrentDebt())) entity.setCurrentDebt(0.0);
            if (GenericUtil.isNull(entity.getHasOverdueDebt())) entity.setHasOverdueDebt(false);
            return repository.save(entity);
        }));
    }

    @Transactional
    @Override
    public Mono<Credit> update(String id, Credit entity) {
        return repository.findById(id).flatMap(existing -> {
            entity.setId(existing.getId());
            return repository.save(entity);
        });
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

}



