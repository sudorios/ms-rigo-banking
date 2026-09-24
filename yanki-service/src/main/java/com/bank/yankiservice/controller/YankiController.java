package com.bank.yankiservice.controller;

import com.bank.yankiservice.model.YankiWallet;
import com.bank.yankiservice.service.YankiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/yanki")
@RequiredArgsConstructor
public class YankiController {

    private final YankiService service;

    @GetMapping
    public Flux<YankiWallet> getAll() {
        return service.findAll();
    }

    @GetMapping("/phone/{phoneNumber}")
    public Mono<YankiWallet> getByPhone(@PathVariable String phoneNumber) {
        return service.findByPhoneNumber(phoneNumber);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<YankiWallet> create(@RequestBody YankiWallet wallet) {
        return service.create(wallet);
    }

    @PostMapping("/associate")
    public Mono<YankiWallet> associateDebitCard(@RequestParam String phone, @RequestParam String card) {
        return service.associateDebitCard(phone, card);
    }

    @PostMapping("/transfer")
    public Mono<Boolean> transfer(@RequestParam String fromPhone, @RequestParam String toPhone, @RequestParam BigDecimal amount) {
        return service.sendMoney(fromPhone, toPhone, amount);
    }
}
