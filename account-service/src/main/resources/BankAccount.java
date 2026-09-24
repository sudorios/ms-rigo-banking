package com.bank.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "$(BankAccount.ToLower())s")
public class BankAccount {
    @Id
    private String id;
    private String accountNumber;
    private String customerId;
    private String accountType;
    private Double balance;
    private Double minAverageMonthlyBalance;
    private Double maintenanceFee;
    private Integer maxMonthlyTransactions;
    private Double transactionFee;
    private List<String> holders;
    private List<String> authorizedSigners;
    private String status;
    private LocalDateTime createdAt;
}
