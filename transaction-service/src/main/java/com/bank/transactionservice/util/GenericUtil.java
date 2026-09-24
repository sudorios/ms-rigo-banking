package com.bank.transactionservice.util;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenericUtil {
    
    private GenericUtil() {
        // Restringe la instanciacion
    }

    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static String getCurrentFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
    
    public static boolean isNull(Object obj) {
        return obj == null;
    }
    
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }
}
