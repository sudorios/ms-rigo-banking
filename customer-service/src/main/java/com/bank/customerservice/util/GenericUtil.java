package com.bank.customerservice.util;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenericUtil {
    
    private GenericUtil() {
        // Restringe la instanciación
    }

    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static String getCurrentFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
