package com.bank.customerservice.service.impl;

import com.bank.customerservice.model.Customer;
import com.bank.customerservice.repository.CustomerRepository;
import com.bank.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.bank.customerservice.util.Constants;
import com.bank.customerservice.util.GenericUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final ReactiveRedisTemplate<String, Customer> redisTemplate;
    private static final String CACHE_KEY = "Customer:";

    @Transactional(readOnly = true)
    @Override
    public Flux<Customer> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Customer> findById(String id) {
        return redisTemplate.opsForValue().get(CACHE_KEY + id)
                .switchIfEmpty(repository.findById(id)
                        .flatMap(customer -> redisTemplate.opsForValue().set(CACHE_KEY + id, customer)
                                .thenReturn(customer)));
    }

    @Transactional
    @Override
    public Mono<Customer> save(Customer entity) {
        if (!Constants.CUSTOMER_TYPE_PERSONAL.equalsIgnoreCase(entity.getCustomerType()) && !Constants.CUSTOMER_TYPE_BUSINESS.equalsIgnoreCase(entity.getCustomerType())) {
            return Mono.error(new RuntimeException(Constants.ERROR_INVALID_CUSTOMER_TYPE));
        }
        entity.setCreatedAt(LocalDateTime.now());
        if (GenericUtil.isNull(entity.getStatus())) {
            entity.setStatus(Constants.STATUS_ACTIVE);
        }
        return repository.save(entity)
                .flatMap(savedCustomer -> redisTemplate.opsForValue().set(CACHE_KEY + savedCustomer.getId(), savedCustomer)
                        .thenReturn(savedCustomer));
    }

    @Transactional
    @Override
    public Mono<Customer> update(String id, Customer entity) {
        return repository.findById(id).flatMap(existing -> {
            if (GenericUtil.isNotNull(entity.getName())) existing.setName(entity.getName());
            if (GenericUtil.isNotNull(entity.getLastName())) existing.setLastName(entity.getLastName());
            if (GenericUtil.isNotNull(entity.getStatus())) existing.setStatus(entity.getStatus());
            return repository.save(existing)
                    .flatMap(savedCustomer -> redisTemplate.opsForValue().set(CACHE_KEY + savedCustomer.getId(), savedCustomer)
                            .thenReturn(savedCustomer));
        });
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id)
                .then(redisTemplate.opsForValue().delete(CACHE_KEY + id).then());
    }
}
