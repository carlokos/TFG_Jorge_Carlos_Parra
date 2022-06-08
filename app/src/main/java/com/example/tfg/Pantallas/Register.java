package com.example.tfg.Pantallas;

import static com.example.tfg.Constants.URL_BASE;
import static com.example.tfg.Constants.apiManager;
import static com.example.tfg.Pantallas.Login.UM;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tfg.Controlador.AccessNetwork;
import com.example.tfg.Controlador.ApiManager;
import com.example.tfg.Controlador.UserManager;
import com.example.tfg.Modelo.User;
import com.example.tfg.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Se encarga de crear un usuario nuevo y registrarlo en la base de datos, al terminar
 * vuelve a la pantalla de login
 */
public class Register extends AppCompatActivity {
    EditText email, pw, name, subname, telf;
    Button add, back;

    private String mail, pass, nom, subnom, tef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.txtAddEmail);
        pw = findViewById(R.id.txtAddPw);
        name = findViewById(R.id.txtAddName);
        subname = findViewById(R.id.txtAddSubnames);
        telf = findViewById(R.id.txtAddTelf);

        add = findViewById(R.id.btnAdd);
        back = findViewById(R.id.btnVolver);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = email.getText().toString();
                pass = pw.getText().toString();
                nom = name.getText().toString();
                subnom = subname.getText().toString();
                tef = telf.getText().toString();
                if(!pass.isEmpty() && !nom.isEmpty()
                        && !subnom.isEmpty() && !tef.isEmpty()) {
                            createUser(mail, pass, nom, subnom, Integer.parseInt(tef));
                } else{
                    Toast("Por favor, relleno todos los campos");
                }
            }
        });
    }

    public void createUser(String email, String pw, String name, String subname, int Telf){
        /*
            Creamos un usuario sin ID, para que no genere conflicto con la base de datos externa
         */
        User u = new User();

        u.setName(name);
        u.setPassword(pw);
        u.setTelf(Telf);
        u.setSubname(subname);
        u.setEmail(email);

        Call<User> call = apiManager.postUser(u);

        /**
         * La razon por la que onResponse es donde controlamos la excepcion es porque
         * cuando da error el body del response no esta nulo (ya que devuelve el fallo) y retrofit
         * lo situa en ese sitio al no ser nulo, mientras que ha postear algo con exito el body
         * del response es nulo, asi que lo situa en onFailure
         */
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast("Correo ya registrado, use uno diferente");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast("Usuario registrado exitosamente");
            }
        });

        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }

    public void Toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}