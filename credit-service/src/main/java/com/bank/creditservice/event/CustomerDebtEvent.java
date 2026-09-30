package com.bank.creditservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDebtEvent {
    private String customerId;
    private Boolean hasOverdueDebt;
}
