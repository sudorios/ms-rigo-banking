package com.demo.example.cars.controller;

import com.demo.example.cars.model.Car;
import com.demo.example.cars.service.CarService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class CarController {

  private final CarService carService;

  public CarController(CarService carService) {
      this.carService = carService;
  }

  @GetMapping("/car")
  public List<Car> listCars(@RequestHeader(required = false) Map<String, String> headers) {
    return carService.listCar();
  }

  @GetMapping("/car/{id}")
  public Car getCars(@RequestHeader(required = false) Map<String, String> headers, @PathVariable String id) {
    return carService.getCar(id);
  }

  @PutMapping("/car/{id}")
  public Car deleteCar(@RequestHeader(required = false) Map<String, String> headers, @PathVariable String id) {
    return carService.deleteCar(id);
  }

  @DeleteMapping("/car/{id}")
  public Car deleteCars(@RequestHeader(required = false) Map<String, String> headers, @PathVariable String id) {
    return carService.deleteCar(id);
  }

}

