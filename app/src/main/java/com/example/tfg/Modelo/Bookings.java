package com.example.tfg.Modelo;

import com.google.gson.annotations.SerializedName;

public class Bookings {
    //Retrofit a la hora de hacer un post ignora los datos tipo Integer, nos viene bien
    //ya que nuestro ID es autoicrementar y lo queremos copiar de la base de datos externa
    //pero no usar el campo a la hora de hacer una llamada con Post
    Integer Id;
    int Pass_Id;
    @SerializedName("Session_date")
    String Session_date;
    int Num_people;
    double Price;
    char Prepaid;
    int User_Id;
    int Service_Id;

    public Bookings(Integer id, int pass_Id, String session_date, int num_people, double price, char prepaid, int user_Id, int service_Id) {
        Id = id;
        Pass_Id = pass_Id;
        Session_date = session_date;
        Num_people = num_people;
        Price = price;
        Prepaid = prepaid;
        User_Id = user_Id;
        Service_Id = service_Id;
    }

    public Bookings() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public int getPass_Id() {
        return Pass_Id;
    }

    public void setPass_Id(int pass_Id) {
        Pass_Id = pass_Id;
    }

    public int getNum_people() {
        return Num_people;
    }

    public void setNum_people(int num_people) {
        Num_people = num_people;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public int getService_Id() {
        return Service_Id;
    }

    public void setService_Id(int service_Id) {
        Service_Id = service_Id;
    }

    public String getSession_date() {
        return Session_date;
    }

    public void setSession_date(String session_date) {
        Session_date = session_date;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public char getPrepaid() {
        return Prepaid;
    }

    public void setPrepaid(char prepaid) {
        Prepaid = prepaid;
    }

    @Override
    public String toString() {
        return "Bookings{" +
                "Pass_Id=" + Pass_Id +
                ", Session_date='" + Session_date + '\'' +
                ", Num_people=" + Num_people +
                ", Price=" + Price +
                ", Prepaid=" + Prepaid +
                ", User_Id=" + User_Id +
                ", Service_Id=" + Service_Id +
                '}';
    }
}
