package com.bank.transactionservice.service.impl;

import com.bank.transactionservice.model.Transaction;
import com.bank.transactionservice.repository.TransactionRepository;
import com.bank.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.bank.transactionservice.util.Constants;
import com.bank.transactionservice.util.GenericUtil;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    @Transactional(readOnly = true)
    @Override
    public Flux<Transaction> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Transaction> findById(String id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public Mono<Transaction> save(Transaction entity) {
        if (GenericUtil.isNull(entity.getAmount()) || entity.getAmount() <= 0) {
            return Mono.error(new RuntimeException(Constants.ERROR_INVALID_AMOUNT));
        }
        
        if (!Constants.TX_TYPE_DEPOSIT.equalsIgnoreCase(entity.getTransactionType()) && 
            !Constants.TX_TYPE_WITHDRAWAL.equalsIgnoreCase(entity.getTransactionType()) &&
            !Constants.TX_TYPE_PAYMENT.equalsIgnoreCase(entity.getTransactionType())) {
            return Mono.error(new RuntimeException(Constants.ERROR_INVALID_TX_TYPE));
        }

        entity.setTransactionDate(LocalDateTime.now());
        if (GenericUtil.isNull(entity.getTransactionNumber())) {
            entity.setTransactionNumber(GenericUtil.generateUniqueId());
        }
        if (GenericUtil.isNull(entity.getFee())) {
            entity.setFee(0.0);
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public Mono<Transaction> update(String id, Transaction entity) {
        return repository.findById(id).flatMap(existing -> {
            entity.setId(existing.getId());
            if (GenericUtil.isNotNull(entity.getAmount())) existing.setAmount(entity.getAmount());
            if (GenericUtil.isNotNull(entity.getFee())) existing.setFee(entity.getFee());
            return repository.save(existing);
        });
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<Transaction> getHistoryByProduct(String productId, String startDate, String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return repository.findHistoryByProductId(productId, start, end);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<Transaction> getLatest10ByCard(String creditCardId) {
        return repository.findTop10ByProductId(creditCardId).take(Constants.LATEST_TRANSACTIONS_LIMIT);
    }
}
