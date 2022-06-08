package com.example.tfg.Controlador;

import static com.example.tfg.Constants.BOOKING_ID;
import static com.example.tfg.Constants.BOOKING_NUM_PEOPLE;
import static com.example.tfg.Constants.BOOKING_PASS_ID;
import static com.example.tfg.Constants.BOOKING_PREPAID;
import static com.example.tfg.Constants.BOOKING_PRICE;
import static com.example.tfg.Constants.BOOKING_SERVICE_ID;
import static com.example.tfg.Constants.BOOKING_SESSION_DATE;
import static com.example.tfg.Constants.BOOKING_USER_ID;
import static com.example.tfg.Constants.PASS_HOUR;
import static com.example.tfg.Constants.PASS_ID;
import static com.example.tfg.Constants.TABLE_BOOKING;
import static com.example.tfg.Constants.TABLE_PASS;
import static com.example.tfg.Constants.TABLE_USER;
import static com.example.tfg.Constants.USER_EMAIL;
import static com.example.tfg.Constants.apiManager;
import static com.example.tfg.Pantallas.MainScreen.ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tfg.Modelo.Bookings;
import com.example.tfg.Modelo.Passes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingManager {
    Context context;
    Database sql;
    SQLiteDatabase BD;

    public BookingManager(Context context) {
        this.context = context;

        sql = new Database(context, TABLE_BOOKING, null, 1);
        BD = sql.getWritableDatabase();
    }

    public void RegisterBooking(Bookings b){
        ContentValues registro = new ContentValues();

        registro.put(BOOKING_ID, b.getId());
        registro.put(BOOKING_PASS_ID, b.getPass_Id());
        registro.put(BOOKING_SESSION_DATE, b.getSession_date());
        registro.put(BOOKING_NUM_PEOPLE, b.getNum_people());
        registro.put(BOOKING_PRICE, b.getPrice());
        registro.put(BOOKING_PREPAID, String.valueOf(b.getPrepaid()));
        registro.put(BOOKING_USER_ID, b.getUser_Id());
        registro.put(BOOKING_SERVICE_ID, b.getService_Id());

        Log.d("Y", registro.toString());

        BD.insert(TABLE_BOOKING, null, registro);
    }

    public boolean CheckBooking(int id){
        boolean existe = false;
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_BOOKING, null);
        int colId = cursor.getColumnIndex(BOOKING_ID);
        if(cursor != null && cursor.moveToFirst()){
            do{
                if(cursor.getInt(colId) == id){
                    existe = true;
                }
            } while(cursor.moveToNext());
        }
        return existe;
    }

    public ArrayList<Bookings> listReserves(int id){
        ArrayList<Bookings> bookings = new ArrayList<Bookings>();
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_BOOKING  + " WHERE " + BOOKING_USER_ID + " = " + id, null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Bookings b = new Bookings(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getDouble(4),
                        (char) cursor.getShort(5),
                        cursor.getInt(6),
                        cursor.getInt(7)
                );
                bookings.add(b);
            }while(cursor.moveToNext());
        }
        return bookings;
    }

    public void removerReserve(int id){
        BD.delete(TABLE_BOOKING, BOOKING_ID + " = " + id, null);
    }
}
