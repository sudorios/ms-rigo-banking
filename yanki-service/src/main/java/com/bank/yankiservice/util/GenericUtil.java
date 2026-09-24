package com.bank.yankiservice.util;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenericUtil {
    
    private GenericUtil() {
        // Restringe la instanciación
    }

    /**
     * Genera un identificador único (UUID) para transacciones.
     */
    public static String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Obtiene la fecha y hora actual en un formato estándar.
     */
    public static String getCurrentFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
