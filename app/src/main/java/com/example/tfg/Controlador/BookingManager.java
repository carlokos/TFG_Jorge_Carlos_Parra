package com.example.tfg.Controlador;

import static com.example.tfg.Constants.BOOKING_ID;
import static com.example.tfg.Constants.BOOKING_NUM_PEOPLE;
import static com.example.tfg.Constants.BOOKING_PASS_ID;
import static com.example.tfg.Constants.BOOKING_PREPAID;
import static com.example.tfg.Constants.BOOKING_PRICE;
import static com.example.tfg.Constants.BOOKING_SERVICE_ID;
import static com.example.tfg.Constants.BOOKING_SESSION_DATE;
import static com.example.tfg.Constants.BOOKING_USER_ID;
import static com.example.tfg.Constants.TABLE_BOOKING;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tfg.Modelo.Booking;

import java.util.ArrayList;

public class BookingManager {
    Context context;
    Database sql;
    SQLiteDatabase BD;

    /**
     * Controlador que se encarga del la tabla Bookings en la BD local
     * @param context
     */
    public BookingManager(Context context) {
        this.context = context;

        sql = new Database(context, TABLE_BOOKING, null, 1);
        BD = sql.getWritableDatabase();
    }

    /**
     * Registra una reserva en la BD local
     * @param b
     */
    public void RegisterBooking(Booking b){
        ContentValues registro = new ContentValues();

        registro.put(BOOKING_ID, b.getId());
        registro.put(BOOKING_PASS_ID, b.getPass_Id());
        registro.put(BOOKING_SESSION_DATE, b.getSession_date());
        registro.put(BOOKING_NUM_PEOPLE, b.getNum_people());
        registro.put(BOOKING_PRICE, b.getPrice());
        registro.put(BOOKING_PREPAID, String.valueOf(b.getPrepaid()));
        registro.put(BOOKING_USER_ID, b.getUser_Id());
        registro.put(BOOKING_SERVICE_ID, b.getService_Id());

        BD.insert(TABLE_BOOKING, null, registro);
    }

    /**
     * Comprueba si una reserva existe a traves de un ID
     * @param id
     * @return boolean
     */
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

    /**
     * Devuelve una array de las reservas de un usuario que identificamos a traves de un ID.
     * Lo usamos en el RecyclerView
     * @param id
     * @return ArrayList<String>
     */
    public ArrayList<Booking> listReserves(int id){
        ArrayList<Booking> bookings = new ArrayList<Booking>();
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_BOOKING  + " WHERE " + BOOKING_USER_ID + " = " + id, null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Booking b = new Booking(
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
