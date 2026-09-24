package com.bank.accountservice.service.impl;

import com.bank.accountservice.model.BankAccount;
import com.bank.accountservice.repository.BankAccountRepository;
import com.bank.accountservice.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository repository;
    private final WebClient.Builder webClientBuilder;

    @Override
    public Flux<BankAccount> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<BankAccount> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<BankAccount> save(BankAccount entity) {
        // Inicializar si son null
        if (entity.getBalance() == null) entity.setBalance(0.0);
        if (entity.getMinAverageMonthlyBalance() == null) entity.setMinAverageMonthlyBalance(0.0);
        
        Mono<CustomerDto> customerMono = webClientBuilder.build().get()
                .uri("http://customer-service/api/v1/customers/" + entity.getCustomerId())
                .retrieve()
                .bodyToMono(CustomerDto.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Cliente no encontrado")));

        Mono<List<CreditDto>> creditsMono = webClientBuilder.build().get()
                .uri("http://credit-service/api/v1/credits/customer/" + entity.getCustomerId())
                .retrieve()
                .bodyToFlux(CreditDto.class)
                .collectList();

        return Mono.zip(customerMono, creditsMono)
                .flatMap(tuple -> {
                    CustomerDto customer = tuple.getT1();
                    java.util.List<CreditDto> credits = tuple.getT2();

                    // 1. Validar deuda vencida
                    boolean hasOverdue = credits.stream().anyMatch(c -> c.getHasOverdueDebt() != null && c.getHasOverdueDebt());
                    if (hasOverdue) {
                        return Mono.error(new RuntimeException("El cliente tiene deuda vencida. No puede abrir cuentas."));
                    }

                    // 2. Reglas VIP y PYME
                    boolean hasCreditCard = credits.stream().anyMatch(c -> "CREDIT_CARD".equalsIgnoreCase(c.getCreditType()));
                    if ("VIP".equalsIgnoreCase(customer.getProfile()) && !hasCreditCard) {
                        return Mono.error(new RuntimeException("Un cliente VIP necesita tener una tarjeta de crédito activa."));
                    }
                    if ("PYME".equalsIgnoreCase(customer.getProfile()) && !hasCreditCard) {
                        return Mono.error(new RuntimeException("Un cliente PYME necesita tener una tarjeta de crédito activa."));
                    }

                    // 3. Reglas de Tipo de Cliente y Cuentas
                    return repository.findAllByCustomerId(customer.getId()).collectList()
                            .flatMap(accounts -> {
                                if ("PERSONAL".equalsIgnoreCase(customer.getCustomerType())) {
                                    // Máximo 1 de cada tipo
                                    long countSameType = accounts.stream()
                                            .filter(a -> a.getAccountType().equalsIgnoreCase(entity.getAccountType()))
                                            .count();
                                    if (countSameType >= 1) {
                                        return Mono.error(new RuntimeException("El cliente personal ya tiene una cuenta de tipo " + entity.getAccountType()));
                                    }
                                } else if ("BUSINESS".equalsIgnoreCase(customer.getCustomerType())) {
                                    // Solo corriente
                                    if (!"CURRENT".equalsIgnoreCase(entity.getAccountType())) {
                                        return Mono.error(new RuntimeException("Un cliente empresarial solo puede tener cuentas corrientes (CURRENT)."));
                                    }
                                    if (entity.getHolders() == null || entity.getHolders().isEmpty()) {
                                        return Mono.error(new RuntimeException("La cuenta empresarial requiere al menos un titular (holder)."));
                                    }
                                }

                                entity.setCreatedAt(LocalDateTime.now());
                                return repository.save(entity);
                            });
                });
    }

    @Override
    public Mono<BankAccount> update(String id, BankAccount entity) {
        return repository.findById(id)
                .flatMap(existing -> {
                    entity.setId(existing.getId());
                    return repository.save(entity);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}

