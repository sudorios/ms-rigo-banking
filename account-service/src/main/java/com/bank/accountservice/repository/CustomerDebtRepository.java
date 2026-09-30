package com.bank.accountservice.repository;

import com.bank.accountservice.model.CustomerDebt;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDebtRepository extends ReactiveMongoRepository<CustomerDebt, String> {
}
