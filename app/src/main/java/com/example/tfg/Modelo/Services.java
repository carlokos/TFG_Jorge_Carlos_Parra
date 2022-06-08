package com.example.tfg.Modelo;

public class Services {
    Integer Id;
    String Type;
    Double Price;

    public Services(Integer id, String type, Double price) {
        Id = id;
        Type = type;
        Price = price;
    }

    public Services() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    @Override
    public String toString() {
        return Type;
    }
}
