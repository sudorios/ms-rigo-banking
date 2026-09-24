package com.bank.yankiservice.util;

public final class Constants {
    private Constants() {
        // Restringe la instanciación
    }

    public static final String INSUFFICIENT_BALANCE_MSG = "Saldo insuficiente para realizar la transferencia";
    public static final String WALLET_NOT_FOUND_MSG = "No se encontró el monedero Yanki con el celular proporcionado";
    public static final String SUCCESS_TRANSFER_MSG = "Transferencia realizada con éxito";
    public static final String DEFAULT_CURRENCY = "PEN";
}
