package com.demo.example.cars.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cars")
public class Car {

  @Id
  private String id;
  private String brand;
  private String model;
  private Double price;
  private String status;

  public Car() {
  }

  public Car(String id, String brand, String model, Double price, String status) {
      this.id = id;
      this.brand = brand;
      this.model = model;
      this.price = price;
      this.status = status;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getBrand() { return brand; }
  public void setBrand(String brand) { this.brand = brand; }

  public String getModel() { return model; }
  public void setModel(String model) { this.model = model; }

  public Double getPrice() { return price; }
  public void setPrice(Double price) { this.price = price; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}
