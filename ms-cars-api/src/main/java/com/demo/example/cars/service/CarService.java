package com.demo.example.cars.service;

import com.demo.example.cars.model.Car;
import java.util.List;

public interface CarService {
   List<Car> listCar();
   Car getCar(String id);
   Car deleteCar(String id);
}

