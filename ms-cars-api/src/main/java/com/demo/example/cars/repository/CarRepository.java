package com.demo.example.cars.repository;

import com.demo.example.cars.model.Car;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {

    @Query("{ '$or': [ { 'status': '1' }, { 'status': true } ] }")
    List<Car> findAvailableCars();

    List<Car> findByStatus(String status);
}

