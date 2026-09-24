package com.bank.transactionservice.service.impl;

import com.bank.transactionservice.model.Transaction;
import com.bank.transactionservice.repository.TransactionRepository;
import com.bank.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.bank.transactionservice.util.Constants;
import com.bank.transactionservice.util.GenericUtil;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    @Override
    public Flux<Transaction> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Transaction> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Transaction> save(Transaction entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Transaction> update(String id, Transaction entity) {
        return repository.findById(id).flatMap(existing -> {
            entity.setId(existing.getId());
            return repository.save(entity);
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<Transaction> getHistoryByProduct(String productId, String startDate, String endDate) {
        java.time.LocalDateTime start = java.time.LocalDateTime.parse(startDate);
        java.time.LocalDateTime end = java.time.LocalDateTime.parse(endDate);
        return repository.findHistoryByProductId(productId, start, end);
    }

    @Override
    public Flux<Transaction> getLatest10ByCard(String creditCardId) {
        return repository.findTop10ByProductId(creditCardId).take(Constants.LATEST_TRANSACTIONS_LIMIT);
    }
}
