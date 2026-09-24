package com.bank.customerservice.service.impl;

import com.bank.customerservice.model.Customer;
import com.bank.customerservice.repository.CustomerRepository;
import com.bank.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public Flux<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Customer> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Customer> save(Customer entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Customer> update(String id, Customer entity) {
        return repository.findById(id)
                .flatMap(existing -> {
                    entity.setId(existing.getId());
                    return repository.save(entity);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}

