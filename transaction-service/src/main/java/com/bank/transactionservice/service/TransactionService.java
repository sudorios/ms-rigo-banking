package com.bank.transactionservice.service;

import com.bank.transactionservice.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Flux<Transaction> findAll();
    Mono<Transaction> findById(String id);
    Mono<Transaction> save(Transaction entity);
    Mono<Transaction> update(String id, Transaction entity);
    Mono<Void> deleteById(String id);
    Flux<Transaction> getHistoryByProduct(String productId, String startDate, String endDate);
    Flux<Transaction> getLatest10ByCard(String creditCardId);
}
