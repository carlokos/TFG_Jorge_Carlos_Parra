package com.example.tfg.Controlador;

import com.example.tfg.Modelo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiManager {

    @GET("user/list")
    Call<List<User>> listUser();

    @POST("user/register")
    Call<User> postUser(@Body User user);
}
