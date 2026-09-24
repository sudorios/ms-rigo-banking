package com.bank.yankiservice.service;

import com.bank.yankiservice.model.YankiWallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface YankiService {
    Flux<YankiWallet> findAll();
    Mono<YankiWallet> findById(String id);
    Mono<YankiWallet> findByPhoneNumber(String phoneNumber);
    Mono<YankiWallet> create(YankiWallet yankiWallet);
    Mono<YankiWallet> associateDebitCard(String phoneNumber, String debitCardNumber);
    Mono<Boolean> sendMoney(String fromPhoneNumber, String toPhoneNumber, BigDecimal amount);
}
