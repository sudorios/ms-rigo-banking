package com.bank.creditservice.controller;

import com.bank.creditservice.model.Credit;
import com.bank.creditservice.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService service;

    @GetMapping
    public Flux<Credit> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Credit> getById(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/customer/{customerId}")
    public Flux<Credit> getByCustomerId(@PathVariable String customerId) {
        return service.findByCustomerId(customerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Credit> create(@RequestBody Credit entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public Mono<Credit> update(@PathVariable String id, @RequestBody Credit entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return service.deleteById(id);
    }
}

