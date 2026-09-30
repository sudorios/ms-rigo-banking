package com.bank.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "debit_cards")
public class DebitCard {
    @Id
    private String id;
    private String cardNumber;
    private String customerId;
    private String primaryAccountId;
    private List<String> associatedAccountIds;
    private LocalDateTime createdAt;
}
