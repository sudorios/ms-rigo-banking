package com.bank.customerservice.model;

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
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    private String documentNumber;
    private String documentType;
    private String name;
    private String lastName;
    private String customerType;
    private String profile;
    private String status;
    private LocalDateTime createdAt;
}

