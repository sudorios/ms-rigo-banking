package com.bank.creditservice.producer;

import com.bank.creditservice.event.CustomerDebtEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerDebtProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendCustomerDebtStatus(String customerId, boolean hasOverdueDebt) {
        CustomerDebtEvent event = new CustomerDebtEvent(customerId, hasOverdueDebt);
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("customer-debt-topic", customerId, payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
