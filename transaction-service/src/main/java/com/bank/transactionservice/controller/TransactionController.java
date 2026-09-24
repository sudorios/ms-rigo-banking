package com.bank.transactionservice.controller;

import com.bank.transactionservice.model.Transaction;
import com.bank.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @GetMapping
    public Flux<Transaction> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Transaction> getById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Transaction> create(@RequestBody Transaction entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public Mono<Transaction> update(@PathVariable String id, @RequestBody Transaction entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return service.deleteById(id);
    }
}
