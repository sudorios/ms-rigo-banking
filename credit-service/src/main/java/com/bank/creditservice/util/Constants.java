package com.bank.creditservice.util;

public final class Constants {
    private Constants() {}
    public static final String CUSTOMER_API_URL = "http://customer-service/api/v1/customers/";
    public static final String ERROR_CUSTOMER_NOT_FOUND = "Cliente no encontrado";
    public static final String ERROR_CUSTOMER_HAS_OVERDUE_DEBT = "El cliente tiene deuda vencida";
    public static final String CUSTOMER_TYPE_PERSONAL = "PERSONAL";
    public static final String CREDIT_TYPE_PERSONAL = "PERSONAL";
    public static final String ERROR_PERSONAL_CUSTOMER_ONE_CREDIT = "Un cliente personal solo puede tener un credito";
}
