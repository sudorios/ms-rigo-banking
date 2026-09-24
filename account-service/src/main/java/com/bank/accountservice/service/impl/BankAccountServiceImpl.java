package com.bank.accountservice.service.impl;

import com.bank.accountservice.model.BankAccount;
import com.bank.accountservice.repository.BankAccountRepository;
import com.bank.accountservice.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository repository;

    @Override
    public Flux<BankAccount> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<BankAccount> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<BankAccount> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Mono<BankAccount> save(BankAccount account) {
        // Lógica para asignar número de cuenta, etc.
        return repository.save(account);
    }

    @Override
    public Mono<BankAccount> update(String id, BankAccount account) {
        return repository.findById(id)
                .flatMap(existingAccount -> {
                    existingAccount.setBalance(account.getBalance());
                    // Actualizar otros campos
                    return repository.save(existingAccount);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}

