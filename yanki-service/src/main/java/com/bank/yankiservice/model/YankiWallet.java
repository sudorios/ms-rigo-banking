package com.bank.yankiservice.model;

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
@Document(collection = "yanki_wallets")
public class YankiWallet {
    @Id
    private String id;
    private String documentNumber; // DNI, CEX, Pasaporte
    private String phoneNumber;
    private String imei;
    private String email;
    private BigDecimal balance;
    private String associatedDebitCardNumber; // Opcional
}
