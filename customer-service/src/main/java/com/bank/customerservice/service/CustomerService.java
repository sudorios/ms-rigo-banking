package com.bank.customerservice.service;

import com.bank.customerservice.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<Customer> findAll();
    Mono<Customer> findById(String id);
    Mono<Customer> save(Customer entity);
    Mono<Customer> update(String id, Customer entity);
    Mono<Void> deleteById(String id);
}
