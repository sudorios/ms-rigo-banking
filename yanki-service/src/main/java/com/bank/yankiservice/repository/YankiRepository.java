package com.bank.yankiservice.repository;

import com.bank.yankiservice.model.YankiWallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface YankiRepository extends ReactiveMongoRepository<YankiWallet, String> {
    Mono<YankiWallet> findByPhoneNumber(String phoneNumber);
    Mono<YankiWallet> findByDocumentNumber(String documentNumber);
}
