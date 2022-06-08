package com.example.tfg.Modelo;

public class User {
    //Retrofit a la hora de hacer un post ignora los datos tipo Integer, nos viene bien
    //ya que nuestro ID es autoicrementar y lo queremos copiar de la base de datos externa
    //pero no usar el campo a la hora de hacer una llamada con Post
    Integer Id;
    String Name, Subname, Password, Email;
    int Telf;

    public User() {
    }

    public User(Integer id, String name, String subaname, String password, String email, int telf) {
        Id = id;
        Name = name;
        Subname = subaname;
        Password = password;
        Email = email;
        Telf = telf;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSubname() {
        return Subname;
    }

    public void setSubname(String subname) {
        Subname = subname;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getTelf() {
        return Telf;
    }

    public void setTelf(int telf) {
        Telf = telf;
    }
}
