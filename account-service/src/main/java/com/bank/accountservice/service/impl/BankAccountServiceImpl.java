package com.bank.accountservice.service.impl;

import com.bank.accountservice.dto.CustomerDto;
import com.bank.accountservice.model.BankAccount;
import com.bank.accountservice.repository.BankAccountRepository;
import com.bank.accountservice.service.BankAccountService;
import com.bank.accountservice.util.Constants;
import com.bank.accountservice.util.GenericUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository repository;
    private final WebClient.Builder webClientBuilder;

    private static final String CUSTOMER_SERVICE_URL = "http://customer-service/api/v1/customers/";

    @Transactional(readOnly = true)
    @Override
    public Flux<BankAccount> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<BankAccount> findById(String id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<BankAccount> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Mono<BankAccount> save(BankAccount account) {
        return webClientBuilder.build().get().uri(CUSTOMER_SERVICE_URL + account.getCustomerId()).retrieve().bodyToMono(CustomerDto.class).switchIfEmpty(Mono.error(new RuntimeException("Cliente no encontrado"))).flatMap(customer -> {
            String type = customer.getCustomerType().toUpperCase();
            String accType = account.getAccountType().toUpperCase();
            if (type.equals(Constants.CLIENT_PERSONAL)) {
                return repository.findByCustomerId(account.getCustomerId()).filter(acc -> acc.getAccountType().equalsIgnoreCase(accType)).hasElements().flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("El cliente personal ya tiene una cuenta de tipo " + accType));
                    }
                    return saveAccountWithDefaults(account, type);
                });
            } else if (type.equals(Constants.CLIENT_BUSINESS)) {
                if (accType.equals(Constants.ACCOUNT_SAVINGS) || accType.equals(Constants.ACCOUNT_FIXED)) {
                    return Mono.error(new RuntimeException("El cliente empresarial no puede tener una cuenta de Ahorro o Plazo Fijo"));
                }
                return saveAccountWithDefaults(account, type);
            } else {
                return Mono.error(new RuntimeException("Tipo de cliente desconocido"));
            }
        });
    }

    private Mono<BankAccount> saveAccountWithDefaults(BankAccount account, String customerType) {
        account.setAccountNumber(GenericUtil.generateUniqueId());
        account.setCreatedAt(GenericUtil.getCurrentFormattedDate());
        if (GenericUtil.isNull(account.getBalance())) {
            account.setBalance(BigDecimal.ZERO);
        }
        String accType = account.getAccountType().toUpperCase();
        if (accType.equals(Constants.ACCOUNT_SAVINGS)) {
            account.setMaintenanceFree(1);
            account.setMaxMovements(5);
        } else if (accType.equals(Constants.ACCOUNT_CURRENT)) {
            account.setMaintenanceFree(0);
            account.setMaxMovements(-1);
        } else if (accType.equals(Constants.ACCOUNT_FIXED)) {
            account.setMaintenanceFree(1);
            account.setMaxMovements(1);
        }
        return repository.save(account);
    }

    @Override
    public Mono<BankAccount> update(String id, BankAccount account) {
        return repository.findById(id).flatMap(existingAccount -> {
            if (GenericUtil.isNotNull(account.getBalance())) existingAccount.setBalance(account.getBalance());
            if (GenericUtil.isNotNull(account.getMaxMovements()))
                existingAccount.setMaxMovements(account.getMaxMovements());
            if (GenericUtil.isNotNull(account.getMaintenanceFree()))
                existingAccount.setMaintenanceFree(account.getMaintenanceFree());
            return repository.save(existingAccount);
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}






