package com.demo.example.cars.service.impl;

import com.demo.example.cars.model.Car;
import com.demo.example.cars.repository.CarRepository;
import com.demo.example.cars.service.CarService;

import java.util.List;

import com.demo.example.cars.exception.CarNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carsRepository;

    public CarServiceImpl(CarRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    @Override
    public List<Car> listCar() {
        return carsRepository.findAvailableCars();
    }

    @Override
    public Car getCar(String id) {
        return carsRepository.findById(id).orElseThrow(() -> new CarNotFoundException("No existe id: " + id));
    }

    @Override
    public Car deleteCar(String id) {
        Car car = getCar(id);
        car.setStatus("0");
        return carsRepository.save(car);
    }

}

