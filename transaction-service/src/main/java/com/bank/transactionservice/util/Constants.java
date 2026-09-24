package com.bank.transactionservice.util;

public final class Constants {
    private Constants() {}

    public static final String NOT_FOUND_MSG = "Registro no encontrado";
    public static final String SUCCESS_MSG = "Operacion realizada con exito";
    public static final String ERROR_MSG = "Ocurrio un error inesperado";
    public static final int LATEST_TRANSACTIONS_LIMIT = 10;
    
    public static final String TX_TYPE_DEPOSIT = "DEPOSITO";
    public static final String TX_TYPE_WITHDRAWAL = "RETIRO";
    public static final String TX_TYPE_PAYMENT = "PAGO";
    
    public static final String ERROR_INVALID_AMOUNT = "El monto de la transaccion debe ser mayor a cero";
    public static final String ERROR_INVALID_TX_TYPE = "Tipo de transaccion invalida. Use DEPOSITO, RETIRO o PAGO";
}
