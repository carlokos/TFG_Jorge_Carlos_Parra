package com.example.tfg.Controlador;

import static com.example.tfg.Constants.TABLE_USER;
import static com.example.tfg.Constants.USER_EMAIL;
import static com.example.tfg.Constants.USER_ID;
import static com.example.tfg.Constants.USER_NAME;
import static com.example.tfg.Constants.USER_PW;
import static com.example.tfg.Constants.USER_SUBNAME;
import static com.example.tfg.Constants.USER_TELF;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tfg.Modelo.User;

import java.util.regex.Pattern;

public class UserManager {
    Context context;
    Database sql;
    SQLiteDatabase BD;

    public UserManager(Context context){
        this.context = context;

        sql = new Database(context, TABLE_USER, null, 1);
        BD = sql.getWritableDatabase();
    }

    /**
     * registra un usuario en la base de datos local
     * @param u
     */
    public void RegisterUser(User u){
        ContentValues registro = new ContentValues();

        registro.put(USER_ID, u.getId());
        registro.put(USER_NAME, u.getName());
        registro.put(USER_SUBNAME, u.getSubname());
        registro.put(USER_TELF, u.getTelf());
        registro.put(USER_EMAIL, u.getEmail());
        registro.put(USER_PW, u.getPassword());

        BD.insert(TABLE_USER, null, registro);
    }

    public boolean Login(String email, String pw){
        boolean log = false;
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_USER, null);
        int colEmail = cursor.getColumnIndex(USER_EMAIL);
        int colPw = cursor.getColumnIndex(USER_PW);
        if(cursor != null && cursor.moveToFirst()){
            do{
                if (cursor.getString(colEmail).equals(email) && cursor.getString(colPw).equals(pw)){
                    log = true;
                }
            } while(cursor.moveToNext());
        }
        return log;
    }

    public boolean CheckUser(String email){
        boolean existe = false;
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_USER, null);
        int columna = cursor.getColumnIndex(USER_EMAIL);
        if(cursor != null && cursor.moveToFirst()){
            do{
                if(cursor.getString(columna).equals(email)){
                    existe = true;
                }
            } while(cursor.moveToNext());
        }
        return existe;
    }

    public boolean validateEmail(EditText email){
        String input = email.getText().toString();

        if(!input.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches()){
            return true;
        } else{
            return false;
        }
    }
}
