package com.bank.creditservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "$(Credit.ToLower())s")
public class Credit {
    @Id
    private String id;
    private String creditNumber;
    private String customerId;
    private String creditType;
    private Double limitAmount;
    private Double availableBalance;
    private Double currentDebt;
    private Boolean hasOverdueDebt;
    private String status;
    private LocalDateTime createdAt;
}
