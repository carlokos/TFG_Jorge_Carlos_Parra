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

    public PassManager(Context context) {
        this.context = context;

        sql = new Database(context, TABLE_PASS, null, 1);
        BD = sql.getWritableDatabase();
    }

    public void RegisterPass(Passes p){
        ContentValues registro = new ContentValues();

        registro.put(PASS_ID, p.getId());
        registro.put(PASS_HOUR, p.getHour());

        BD.insert(TABLE_PASS, null, registro);

    }

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
}
