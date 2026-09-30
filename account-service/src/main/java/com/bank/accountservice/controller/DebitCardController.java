package com.bank.accountservice.controller;

import com.bank.accountservice.model.DebitCard;
import com.bank.accountservice.service.DebitCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/debit-cards")
@RequiredArgsConstructor
public class DebitCardController {

    private final DebitCardService service;

    @GetMapping
    public Flux<DebitCard> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<DebitCard> findById(@PathVariable String id) {
        return service.findById(id);
    }
    
    @GetMapping("/customer/{customerId}")
    public Flux<DebitCard> findByCustomerId(@PathVariable String customerId) {
        return service.findByCustomerId(customerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DebitCard> save(@RequestBody DebitCard debitCard) {
        return service.save(debitCard);
    }

    @PutMapping("/{id}")
    public Mono<DebitCard> update(@PathVariable String id, @RequestBody DebitCard debitCard) {
        return service.update(id, debitCard);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable String id) {
        return service.deleteById(id);
    }
}
