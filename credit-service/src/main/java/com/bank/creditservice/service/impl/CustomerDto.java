package com.bank.creditservice.service.impl;

import lombok.Data;

@Data
public class CustomerDto {
    private String id;
    private String documentNumber;
    private String customerType;
    private String profile;
}
