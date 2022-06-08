package com.example.tfg.Controlador;

import static com.example.tfg.Constants.PASS_HOUR;
import static com.example.tfg.Constants.PASS_ID;
import static com.example.tfg.Constants.SERVICE_ID;
import static com.example.tfg.Constants.SERVICE_PRICE;
import static com.example.tfg.Constants.SERVICE_TYPE;
import static com.example.tfg.Constants.TABLE_PASS;
import static com.example.tfg.Constants.TABLE_SERVICE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tfg.Modelo.Passes;
import com.example.tfg.Modelo.Services;

public class ServiceManager {
    Context context;
    Database sql;
    SQLiteDatabase BD;

    public ServiceManager(Context context) {
        this.context = context;

        sql = new Database(context, TABLE_SERVICE, null, 1);
        BD = sql.getWritableDatabase();
    }

    public void RegisterService(Services s){
        ContentValues registro = new ContentValues();

        registro.put(SERVICE_ID, s.getId());
        registro.put(SERVICE_TYPE, s.getType());
        registro.put(SERVICE_PRICE, s.getPrice());

        BD.insert(TABLE_SERVICE, null, registro);
    }

    public boolean checkService(int id){
        boolean existe = false;
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_SERVICE, null);
        int col = cursor.getColumnIndex(SERVICE_ID);
        if(cursor != null && cursor.moveToFirst()){
            do{
                if(cursor.getInt(col) == id){
                    existe = true;
                }
            } while(cursor.moveToNext());
        }
        return existe;
    }

    public String returnType(int id){
        String Type = "";
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_SERVICE, null);
        int colId = cursor.getColumnIndex(SERVICE_ID);
        int colType = cursor.getColumnIndex(SERVICE_TYPE);
        if(cursor != null && cursor.moveToFirst()){
            do{
                if(cursor.getInt(colId) == id){
                    Type = cursor.getString(colType);
                }
            } while(cursor.moveToNext());
        }
        return Type;
    }
}
