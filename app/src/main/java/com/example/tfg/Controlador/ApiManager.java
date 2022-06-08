package com.example.tfg.Controlador;

import com.example.tfg.Modelo.Bookings;
import com.example.tfg.Modelo.Passes;
import com.example.tfg.Modelo.Services;
import com.example.tfg.Modelo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiManager {

    @GET("user/list")
    Call<List<User>> listUser();

    @POST("user/register")
    Call<User> postUser(@Body User user);

    @GET("pass/list")
    Call<List<Passes>> listPass();

    @GET("service/list")
    Call<List<Services>> listService();

    @GET("booking/list")
    Call<List<Bookings>> listBooking();

    @POST("booking/register")
    Call<Bookings> postBooking(@Body Bookings booking);

    @DELETE("booking/{Id}")
    Call<Void> deleteReserve(@Path("Id") int id);
}
