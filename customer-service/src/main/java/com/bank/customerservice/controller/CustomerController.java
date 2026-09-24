package com.bank.customerservice.controller;

import com.bank.customerservice.model.Customer;
import com.bank.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    public Flux<Customer> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> create(@RequestBody Customer entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public Mono<Customer> update(@PathVariable String id, @RequestBody Customer entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return service.deleteById(id);
    }
}
