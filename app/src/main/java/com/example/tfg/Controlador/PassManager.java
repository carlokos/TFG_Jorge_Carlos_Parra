package com.example.tfg.Controlador;

import static com.example.tfg.Constants.BOOKING_SESSION_DATE;
import static com.example.tfg.Constants.BOOKING_USER_ID;
import static com.example.tfg.Constants.PASS_HOUR;
import static com.example.tfg.Constants.PASS_ID;
import static com.example.tfg.Constants.TABLE_BOOKING;
import static com.example.tfg.Constants.TABLE_PASS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tfg.Modelo.Passes;

public class PassManager {
    Context context;
    Database sql;
    SQLiteDatabase BD;

    /**
     * Controlador que se encarga del la tabla Passes en la BD local
     * @param context
     */
    public PassManager(Context context) {
        this.context = context;

        sql = new Database(context, TABLE_PASS, null, 1);
        BD = sql.getWritableDatabase();
    }

    /**
     * Registra un pase en la BD local
     * @param p
     */
    public void RegisterPass(Passes p){
        ContentValues registro = new ContentValues();

        registro.put(PASS_ID, p.getId());
        registro.put(PASS_HOUR, p.getHour());

        BD.insert(TABLE_PASS, null, registro);

    }

    /**
     * Comprueba si un pase existe a traves de su ID
     * @param id
     * @return boolean
     */
    public boolean checkPass(int id){
        boolean existe = false;
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_PASS, null);
        int col = cursor.getColumnIndex(PASS_ID);
        if(cursor != null && cursor.moveToFirst()){
            do{
                if(cursor.getInt(col) == id){
                    existe = true;
                }
            } while(cursor.moveToNext());
        }
        return existe;
    }

    /**
     * Devuelve la hora de un pase a traves de un ID
     * @param id
     * @return String
     */
    public String returnHour(int id){
        String hour = "";
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_PASS, null);
        int col = cursor.getColumnIndex(PASS_ID);
        int colH = cursor.getColumnIndex(PASS_HOUR);
        if(cursor != null && cursor.moveToFirst()){
            do{
                if(cursor.getInt(col) == id){
                    hour = cursor.getString(colH);
                }
            } while(cursor.moveToNext());
        }
        return hour;
    }
}
