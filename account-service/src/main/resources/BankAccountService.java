package com.bank.accountservice.service;

import com.bank.accountservice.model.BankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountService {

    Flux<BankAccount> findAll();

    Mono<BankAccount> findById(String id);

    Mono<BankAccount> save(BankAccount entity);

    Mono<BankAccount> update(String id, BankAccount entity);

    Mono<Void> deleteById(String id);
}
