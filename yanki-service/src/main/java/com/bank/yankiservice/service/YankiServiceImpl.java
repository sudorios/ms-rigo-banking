package com.bank.yankiservice.service;

import com.bank.yankiservice.model.YankiWallet;
import com.bank.yankiservice.repository.YankiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class YankiServiceImpl implements YankiService {

    private final YankiRepository repository;

    @Transactional(readOnly = true)
    @Override
    public Flux<YankiWallet> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<YankiWallet> findById(String id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<YankiWallet> findByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Mono<YankiWallet> create(YankiWallet yankiWallet) {
        if (yankiWallet.getBalance() == null) {
            yankiWallet.setBalance(BigDecimal.ZERO);
        }
        return repository.save(yankiWallet);
    }

    @Override
    public Mono<YankiWallet> associateDebitCard(String phoneNumber, String debitCardNumber) {
        return repository.findByPhoneNumber(phoneNumber)
                .flatMap(wallet -> {
                    wallet.setAssociatedDebitCardNumber(debitCardNumber);
                    return repository.save(wallet);
                });
    }

    @Override
    public Mono<Boolean> sendMoney(String fromPhoneNumber, String toPhoneNumber, BigDecimal amount) {
        return repository.findByPhoneNumber(fromPhoneNumber)
                .zipWith(repository.findByPhoneNumber(toPhoneNumber))
                .flatMap(tuple -> {
                    YankiWallet from = tuple.getT1();
                    YankiWallet to = tuple.getT2();

                    if (from.getBalance().compareTo(amount) >= 0) {
                        from.setBalance(from.getBalance().subtract(amount));
                        to.setBalance(to.getBalance().add(amount));
                        return repository.save(from)
                                .then(repository.save(to))
                                .thenReturn(true);
                    }
                    return Mono.just(false); // Saldo insuficiente
                })
                .switchIfEmpty(Mono.just(false)); // Cuenta no encontrada
    }
}


