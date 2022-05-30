package com.example.tfg.Pantallas;

import static com.example.tfg.Constants.URL_BASE;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    EditText email, pw;
    Button login, signup;
    UserManager UM;
    ApiManager apiManager;
    public publishTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UM = new UserManager(this);

        email = findViewById(R.id.txtEmail);
        pw = findViewById(R.id.txtPassword);
        login = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.btnRegister);

        if(AccessNetwork.checkNetworkState(this)) {
            mTask = new publishTask();
            mTask.execute();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                String contrasenia = pw.getText().toString();
                if(UM.Login(mail,contrasenia) && UM.validateEmail(email)){
                    Toast("Iniciando sesión");
                } else{
                    Toast("Error, el email o la contraseña son incorrectos");
                    email.setText("");
                    pw.setText("");
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccessNetwork.checkNetworkState(view.getContext())) {
                    loadRegister(view);
                } else{
                    /** TODO mejorar el aviso del internet*/
                    Toast("Se necesita conexion a Internet");
                }
            }
        });
    }

    private void loadRegister(View view){
        Intent i = new Intent(this, Register.class);
        startActivity(i);
    }

    private class publishTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiManager = retrofit.create(ApiManager.class);
            Call<List<User>> call = apiManager.listUser();

            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    List<User> lista = response.body();

                    for(User u : lista){
                        if(!UM.CheckUser(u.getEmail())) {
                            UM.RegisterUser(u);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    public void Toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}