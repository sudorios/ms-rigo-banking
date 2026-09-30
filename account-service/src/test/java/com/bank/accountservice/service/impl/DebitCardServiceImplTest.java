package com.bank.accountservice.service.impl;

import com.bank.accountservice.model.DebitCard;
import com.bank.accountservice.repository.DebitCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebitCardServiceImplTest {

    @Mock
    private DebitCardRepository repository;

    @InjectMocks
    private DebitCardServiceImpl service;

    private DebitCard mockCard;

    @BeforeEach
    void setUp() {
        mockCard = DebitCard.builder()
                .id("card1")
                .cardNumber("123456789")
                .customerId("cust1")
                .primaryAccountId("acc1")
                .associatedAccountIds(List.of("acc1", "acc2"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll_ShouldReturnDebitCards() {
        when(repository.findAll()).thenReturn(Flux.just(mockCard));

        StepVerifier.create(service.findAll())
                .expectNext(mockCard)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnDebitCard() {
        when(repository.findById("card1")).thenReturn(Mono.just(mockCard));

        StepVerifier.create(service.findById("card1"))
                .expectNext(mockCard)
                .verifyComplete();
    }

    @Test
    void save_ShouldSaveAndReturnDebitCard() {
        when(repository.save(any(DebitCard.class))).thenReturn(Mono.just(mockCard));

        StepVerifier.create(service.save(new DebitCard()))
                .expectNext(mockCard)
                .verifyComplete();
    }

    @Test
    void deleteById_ShouldDeleteSuccessfully() {
        when(repository.deleteById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteById("card1"))
                .verifyComplete();
    }
}
