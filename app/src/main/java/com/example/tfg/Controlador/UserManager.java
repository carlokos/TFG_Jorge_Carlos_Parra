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
import android.util.Log;
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

    /**
     * Método que comprueba si un usuario existe en la base de datos local
     * @param email
     * @param pw
     * @return boolean
     */
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

    public Integer getId(String email){
        Integer id = -1;
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_USER, null);
        int colEmail = cursor.getColumnIndex(USER_EMAIL);
        int colId = cursor.getColumnIndex(USER_ID);
        if(cursor != null && cursor.moveToFirst()){
            do{
                if(cursor.getString(colEmail).equals(email)){
                  id = cursor.getInt(colId);
                }
            }while(cursor.moveToNext());
        }
        return id;
    }

    public User getUser(Integer id){
        User u = null;
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_USER, null);
        int colId = cursor.getColumnIndex(USER_ID);
        if(cursor != null && cursor.moveToFirst()){
            do{
                if(cursor.getInt(colId) == id){
                    u = new User(id, cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getInt(5));
                }
            }while(cursor.moveToNext());
        }
        return u;
    }

    public boolean CheckUser(User u){
        boolean existe = false;
        Cursor cursor = BD.rawQuery("SELECT * FROM " + TABLE_USER, null);
        int colEmail = cursor.getColumnIndex(USER_EMAIL);
        int colId = cursor.getColumnIndex(USER_ID);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Log.d("ID:", u.getId().toString());
                if(cursor.getString(colEmail).equals(u.getEmail())){
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
