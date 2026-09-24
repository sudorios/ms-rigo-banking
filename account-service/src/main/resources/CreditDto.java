package com.bank.accountservice.service.impl;

import lombok.Data;

@Data
public class CreditDto {
    private String id;
    private String creditType;
    private Boolean hasOverdueDebt;
}
