package com.bank.transactionservice.repository;

import com.bank.transactionservice.model.Transaction;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

    @Query("{ '$or': [ { 'sourceProductId': ?0 }, { 'targetProductId': ?0 } ], 'transactionDate': { $gte: ?1, $lte: ?2 } }")
    Flux<Transaction> findHistoryByProductId(String productId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "{ '$or': [ { 'sourceProductId': ?0 }, { 'targetProductId': ?0 } ] }", sort = "{ 'transactionDate': -1 }")
    Flux<Transaction> findTop10ByProductId(String productId);
}
