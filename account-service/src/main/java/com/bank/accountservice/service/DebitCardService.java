package com.bank.accountservice.service;

import com.bank.accountservice.model.DebitCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCardService {
    Flux<DebitCard> findAll();
    Mono<DebitCard> findById(String id);
    Flux<DebitCard> findByCustomerId(String customerId);
    Mono<DebitCard> save(DebitCard debitCard);
    Mono<DebitCard> update(String id, DebitCard debitCard);
    Mono<Void> deleteById(String id);
}
