package com.example.tfg.Pantallas;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;
import static com.example.tfg.Constants.apiManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.tfg.Controlador.AccessNetwork;
import com.example.tfg.Controlador.UserManager;
import com.example.tfg.Modelo.User;
import com.example.tfg.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Clase encargada del login, se puede acceder sin internet pero al iniciar la app, si
 * esta conectada a internet se sincronizara todos los datos de los usuarios
 */
public class Login extends AppCompatActivity {
    EditText email, pw;
    Button login, signup;
    public static UserManager UM;
    private publishTask mTask;

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
                    logIn(view, mail);
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
                    Toast("Se necesita conexion a Internet");
                }
            }
        });
    }

    private void loadRegister(View view){
        Intent i = new Intent(this, Register.class);
        startActivity(i);
        finish();
    }

    private void logIn(View view, String mail){
        Intent i = new Intent(this, MainScreen.class);
        i.putExtra("Id", UM.getId(mail));
        startActivity(i);
        finish();
    }

    private class publishTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Call<List<User>> call = apiManager.listUser();

            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    List<User> lista = response.body();

                    for(User u : lista){
                        if(!UM.CheckUser(u)) {
                            Log.d("Registrado:", u.getEmail());
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