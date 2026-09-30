package com.bank.accountservice.service.impl;

import com.bank.accountservice.model.DebitCard;
import com.bank.accountservice.repository.DebitCardRepository;
import com.bank.accountservice.service.DebitCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DebitCardServiceImpl implements DebitCardService {

    private final DebitCardRepository repository;

    @Override
    public Flux<DebitCard> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<DebitCard> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<DebitCard> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Mono<DebitCard> save(DebitCard debitCard) {
        debitCard.setCreatedAt(LocalDateTime.now());
        return repository.save(debitCard);
    }

    @Override
    public Mono<DebitCard> update(String id, DebitCard debitCard) {
        return repository.findById(id).flatMap(existing -> {
            if (debitCard.getCardNumber() != null) existing.setCardNumber(debitCard.getCardNumber());
            if (debitCard.getPrimaryAccountId() != null) existing.setPrimaryAccountId(debitCard.getPrimaryAccountId());
            if (debitCard.getAssociatedAccountIds() != null) existing.setAssociatedAccountIds(debitCard.getAssociatedAccountIds());
            return repository.save(existing);
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}
