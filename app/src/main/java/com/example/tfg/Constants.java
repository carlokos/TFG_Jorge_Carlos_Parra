package com.example.tfg;

import android.content.Context;
import android.widget.Toast;

import com.example.tfg.Controlador.ApiManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase donde almacenamos todas las constantes, principalmente la url de la api y los strings
 * necesarios para crear y manejar la base de datos externa mas comodamente
 */
public class Constants {
    /*
     *  URL base para acceder a la base de datos externa
     *  Esta formada por la IP del ordenador y su puerto
     */
    public static String URL_BASE = "http://192.168.1.139:8000/v1/";

    //Retrofit que usamos en toda la aplicacion, lo ponemos aqui para ahorrar codigo
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiManager apiManager = retrofit.create(ApiManager.class);

    //Constantes de la tabla Usuarios
    public static String TABLE_USER = "users";
    public static String USER_ID = "Id";
    public static String USER_NAME = "Name";
    public static String USER_SUBNAME = "Subname";
    public static String USER_TELF = "Telf";
    public static String USER_EMAIL = "Email";
    public static String USER_PW = "Password";

    //Crear tabla usuarios
    public static final String CREAR_TABLA_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_USER +
            "("
                + USER_ID + " INTENGER PRIMARY KEY, " +
                USER_NAME + " TEXT, " +
                USER_SUBNAME + " TEXT, " +
                USER_TELF + " INT, " +
                USER_EMAIL + " TEXT UNIQUE, " +
                USER_PW + " TEXT)";

    //Constantes de la tabla Reservas
    public static String TABLE_BOOKING = "bookings";
    public static String BOOKING_ID = "Id";
    public static String BOOKING_PASS_ID = "Pass_Id";
    public static String BOOKING_SESSION_DATE = "Session_date";
    public static String BOOKING_NUM_PEOPLE = "Num_people";
    public static String BOOKING_PRICE = "Price";
    public static String BOOKING_PREPAID = "Prepaid";
    public static String BOOKING_USER_ID = "User_Id";
    public static String BOOKING_SERVICE_ID = "Service_Id";


    //Constantes de la tabla Servicios
    public static final String TABLE_SERVICE = "services";
    public static final String SERVICE_ID = "Id";
    public static final String SERVICE_TYPE = "Type";
    public static final String SERVICE_PRICE = "Price";

    //Crear tabla servicios
    public static final String CREAR_TABLA_SERVICES = "CREATE TABLE IF NOT EXISTS " + TABLE_SERVICE +
            "("
            + SERVICE_ID + " INTEGER PRIMARY KEY, " +
            SERVICE_TYPE + " TEXT, " +
            SERVICE_PRICE + " DOUBLE)";

    //Constantes de la tabla Passes
    public static String TABLE_PASS = "passes";
    public static String PASS_ID = "Id";
    public static String PASS_HOUR = "Hour";

    //Crear tabla pases
    public static final String CREAR_TABLA_PASSES = "CREATE TABLE IF NOT EXISTS " + TABLE_PASS +
            "("
            + PASS_ID + " INTENGER PRIMARY KEY, " +
            PASS_HOUR + " TEXT)";

    //Crear tabla reservas
    public static final String CREAR_TABLA_BOOKINGS = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKING +
            "("
            + BOOKING_ID + " INTEGER PRIMARY KEY, "
            + BOOKING_PASS_ID + " INTENGER, " +
            BOOKING_SESSION_DATE + " TEXT, " +
            BOOKING_NUM_PEOPLE + " INTEGER, " +
            BOOKING_PRICE + " DOUBLE, " +
            BOOKING_PREPAID + " TEXT, " +
            BOOKING_USER_ID + " INTEGER, " +
            BOOKING_SERVICE_ID + " INTEGER," +
            " FOREIGN KEY (" + BOOKING_USER_ID + ") REFERENCES " + TABLE_USER +"(" + USER_ID + ")," +
            " FOREIGN KEY (" + BOOKING_SERVICE_ID + ") REFERENCES " + TABLE_SERVICE + "(" + SERVICE_ID + ")," +
            " FOREIGN KEY (" + BOOKING_PASS_ID + ") REFERENCES " + TABLE_PASS +"(" + PASS_ID + ")," +
            " UNIQUE (" + BOOKING_SESSION_DATE + ", " + BOOKING_USER_ID + ")" +
            ")";
}
