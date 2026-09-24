package com.bank.transactionservice.model;

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
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String transactionNumber;
    private String sourceProductId;
    private String targetProductId;
    private String productType;
    private String transactionType;
    private Double amount;
    private Double fee;
    private java.time.LocalDateTime transactionDate;
}
