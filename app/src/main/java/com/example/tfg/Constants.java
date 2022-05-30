package com.example.tfg;

import android.content.Context;
import android.widget.Toast;

public class Constants {
    /*URL base para acceder a la base de datos externa
     *  Esta formada por la IP del ordenador y su puerto
     */
    public static String URL_BASE = "http://192.168.1.139:8000/v1/";

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

}
