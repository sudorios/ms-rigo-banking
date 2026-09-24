package com.bank.creditservice.service;

import com.bank.creditservice.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Flux<Credit> findAll();
    Mono<Credit> findById(String id);
    Flux<Credit> findByCustomerId(String customerId);
    Mono<Credit> save(Credit entity);
    Mono<Credit> update(String id, Credit entity);
    Mono<Void> deleteById(String id);
}

