package com.bank.transactionservice.controller;

import com.bank.transactionservice.model.Transaction;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import com.bank.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final TransactionService transactionService;

    @GetMapping("/history/{productId}")
    public Flux<Transaction> getHistoryByProduct(@PathVariable String productId, @RequestParam String startDate, @RequestParam String endDate) {
        return transactionService.getHistoryByProduct(productId, startDate, endDate);
    }

    @GetMapping("/latest-10/{creditCardId}")
    public Flux<Transaction> getLatest10ByCard(@PathVariable String creditCardId) {
        return transactionService.getLatest10ByCard(creditCardId);
    }

}
