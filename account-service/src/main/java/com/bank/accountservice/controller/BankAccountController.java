package com.bank.accountservice.controller;

import com.bank.accountservice.model.BankAccount;
import com.bank.accountservice.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bankaccounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService service;

    @GetMapping
    public Flux<BankAccount> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<BankAccount> getById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BankAccount> create(@RequestBody BankAccount entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public Mono<BankAccount> update(@PathVariable String id, @RequestBody BankAccount entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return service.deleteById(id);
    }
}
