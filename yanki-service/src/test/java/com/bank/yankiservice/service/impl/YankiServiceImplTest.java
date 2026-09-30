package com.bank.yankiservice.service.impl;

import com.bank.yankiservice.model.YankiWallet;
import com.bank.yankiservice.repository.YankiRepository;
import com.bank.yankiservice.service.YankiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YankiServiceImplTest {

    @Mock
    private YankiRepository repository;

    @InjectMocks
    private YankiServiceImpl service;

    private YankiWallet mockWallet;

    @BeforeEach
    void setUp() {
        mockWallet = YankiWallet.builder()
                .id("w1")
                .phoneNumber("999888777")
                .documentNumber("12345678")
                .imei("IMEI123")
                .email("test@yanki.com")
                .balance(new BigDecimal("100.00"))
                .build();
    }

    @Test
    void findAll_ShouldReturnWallets() {
        when(repository.findAll()).thenReturn(Flux.just(mockWallet));

        StepVerifier.create(service.findAll())
                .expectNext(mockWallet)
                .verifyComplete();
    }

    @Test
    void create_ShouldSaveWalletWithZeroBalance() {
        YankiWallet newWallet = new YankiWallet();
        when(repository.save(any(YankiWallet.class))).thenReturn(Mono.just(newWallet));

        StepVerifier.create(service.create(newWallet))
                .expectNext(newWallet)
                .verifyComplete();
    }

    @Test
    void sendMoney_ShouldSucceed_WhenBalanceIsSufficient() {
        YankiWallet from = YankiWallet.builder().phoneNumber("999").balance(new BigDecimal("50.00")).build();
        YankiWallet to = YankiWallet.builder().phoneNumber("888").balance(new BigDecimal("10.00")).build();

        when(repository.findByPhoneNumber("999")).thenReturn(Mono.just(from));
        when(repository.findByPhoneNumber("888")).thenReturn(Mono.just(to));
        when(repository.save(from)).thenReturn(Mono.just(from));
        when(repository.save(to)).thenReturn(Mono.just(to));

        StepVerifier.create(service.sendMoney("999", "888", new BigDecimal("20.00")))
                .expectNext(true)
                .verifyComplete();
    }
}
