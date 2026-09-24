package com.bank.accountservice.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private String id;
    private String customerType; // PERSONAL, EMPRESARIAL
    private String name;
}
