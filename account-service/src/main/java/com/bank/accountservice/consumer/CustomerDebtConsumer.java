package com.bank.accountservice.consumer;

import com.bank.accountservice.event.CustomerDebtEvent;
import com.bank.accountservice.model.CustomerDebt;
import com.bank.accountservice.repository.CustomerDebtRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerDebtConsumer {

    private final CustomerDebtRepository repository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "customer-debt-topic", groupId = "account-service-group")
    public void consumeDebtEvent(String message) {
        try {
            CustomerDebtEvent event = objectMapper.readValue(message, CustomerDebtEvent.class);
            CustomerDebt debt = new CustomerDebt(event.getCustomerId(), event.getHasOverdueDebt());
            // Reactor is asynchronous, so we subscribe to trigger the save
            repository.save(debt).subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
