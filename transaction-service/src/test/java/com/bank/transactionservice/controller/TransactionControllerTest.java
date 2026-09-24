package com.bank.transactionservice.controller;

import com.bank.transactionservice.model.Transaction;
import com.bank.transactionservice.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionService transactionService;

    @Test
    void getAllTransactions() {
        Transaction t = new Transaction();
        t.setId("1");
        t.setTransactionNumber("TX-001");
        t.setAmount(100.0);

        when(transactionService.findAll()).thenReturn(Flux.just(t));

        webTestClient.get()
                .uri("/api/v1/transactions")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].transactionNumber").isEqualTo("TX-001");
    }
}
