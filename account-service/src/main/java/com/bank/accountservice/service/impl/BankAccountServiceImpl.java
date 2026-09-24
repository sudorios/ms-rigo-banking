package com.bank.accountservice.service.impl;

import com.bank.accountservice.dto.CustomerDto;
import com.bank.accountservice.model.BankAccount;
import com.bank.accountservice.repository.BankAccountRepository;
import com.bank.accountservice.service.BankAccountService;
import com.bank.accountservice.util.GenericUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository repository;
    private final WebClient.Builder webClientBuilder;

    private static final String CUSTOMER_SERVICE_URL = "http://customer-service/api/v1/customers/";

    @Override
    public Flux<BankAccount> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<BankAccount> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<BankAccount> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Mono<BankAccount> save(BankAccount account) {
        // 1. Consultar si el cliente existe en el Customer Service
        return webClientBuilder.build().get()
                .uri(CUSTOMER_SERVICE_URL + account.getCustomerId())
                .retrieve()
                .bodyToMono(CustomerDto.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Cliente no encontrado")))
                .flatMap(customer -> {
                    // 2. Validar reglas de negocio según tipo de cliente
                    String type = customer.getCustomerType().toUpperCase();
                    String accType = account.getAccountType().toUpperCase();
                    
                    if (type.equals("PERSONAL")) {
                        // Un cliente personal solo puede tener un máximo de una cuenta de ahorro, una cuenta corriente o a plazo fijo
                        return repository.findByCustomerId(account.getCustomerId())
                                .filter(acc -> acc.getAccountType().equalsIgnoreCase(accType))
                                .hasElements()
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new RuntimeException("El cliente personal ya tiene una cuenta de tipo " + accType));
                                    }
                                    return saveAccountWithDefaults(account, type);
                                });
                    } else if (type.equals("EMPRESARIAL")) {
                        // Un cliente empresarial no puede tener una cuenta de ahorro o de plazo fijo, pero sí múltiples cuentas corrientes.
                        if (accType.equals("AHORRO") || accType.equals("PLAZO_FIJO")) {
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
        if(account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        
        // Reglas de comisiones y movimientos
        String accType = account.getAccountType().toUpperCase();
        if (accType.equals("AHORRO")) {
            account.setMaintenanceFree(1);
            account.setMaxMovements(5); // límite máximo de movimientos mensuales (ejemplo)
        } else if (accType.equals("CORRIENTE")) {
            account.setMaintenanceFree(0); // cobra mantenimiento
            account.setMaxMovements(-1); // sin límite
        } else if (accType.equals("PLAZO_FIJO")) {
            account.setMaintenanceFree(1);
            account.setMaxMovements(1); // un solo movimiento de retiro o depósito (el día de retiro)
        }
        
        return repository.save(account);
    }

    @Override
    public Mono<BankAccount> update(String id, BankAccount account) {
        return repository.findById(id)
                .flatMap(existingAccount -> {
                    if (account.getBalance() != null) existingAccount.setBalance(account.getBalance());
                    if (account.getMaxMovements() != null) existingAccount.setMaxMovements(account.getMaxMovements());
                    if (account.getMaintenanceFree() != null) existingAccount.setMaintenanceFree(account.getMaintenanceFree());
                    return repository.save(existingAccount);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}
