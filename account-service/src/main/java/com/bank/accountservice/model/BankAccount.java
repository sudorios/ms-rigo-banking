package com.bank.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bank_accounts")
public class BankAccount {
    @Id
    private String id;
    private String customerId;
    private String accountNumber;
    private String accountType; // AHORRO, CORRIENTE, PLAZO_FIJO
    private BigDecimal balance;
    private Integer maintenanceFree;
    private Integer maxMovements;
    private String createdAt;
}
