package com.example.tfg.Modelo;

public class Passes {
    int Id;

    String Hour;

    public Passes(int id, String hour) {
        this.Id = id;
        this.Hour = hour;
    }

    public Passes() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        this.Hour = hour;
    }

    @Override
    public String toString() {
        return Hour;
    }
}
