package com.example.tfg.Pantallas;

import static com.example.tfg.Constants.URL_BASE;

import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {
    EditText email, pw, name, subname, telf;
    Button add, back;

    private String mail, pass, nom, subnom, tef;
    private ApiManager apiManager;
    private UserManager UM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        UM = new UserManager(this);

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

                if(AccessNetwork.checkNetworkState(view.getContext())){
                    if(UM.validateEmail(email)){
                        if(!pass.isEmpty()
                            && !nom.isEmpty()
                            && !subnom.isEmpty()
                            && !tef.isEmpty()) {
                            createUser(mail, pass, nom, subnom, Integer.parseInt(tef));
                        } else{
                            Toast("Por favor, relleno todos los campos");
                        }
                    } else{
                        Toast("Email introducido incorrectamente, compruebe el correo");
                    }
                } else{
                    // TODO mejorar el toast
                    Toast("Se necesita acceso a internet");
                }
            }
        });
    }

    public void createUser(String email, String pw, String name, String subname, int Telf){
        User u = new User();

        u.setName(name);
        u.setPassword(pw);
        u.setTelf(Telf);
        u.setSubname(subname);
        u.setEmail(email);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiManager = retrofit.create(ApiManager.class);

        Call<User> call = apiManager.postUser(u);

        /**
         * La razon por la que onResponse es donde controlamos la excepcion es porque
         * cuando da error siempre manda el codigo 500 pero no siempre cuando hace un post
         * manda el codigo 201 si no que no manda codigo del respond (aunque se haga el metodo
         * Post correctamente) asi que Retrofit lo envia directamente a onFailure
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
    }

    public void Toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}