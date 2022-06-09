package com.example.tfg.Pantallas;

import static com.example.tfg.Constants.apiManager;
import static com.example.tfg.Pantallas.Login.UM;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.Controlador.AccessNetwork;
import com.example.tfg.Controlador.BookingManager;
import com.example.tfg.Controlador.PassManager;
import com.example.tfg.Controlador.ServiceManager;
import com.example.tfg.Modelo.Booking;
import com.example.tfg.Modelo.Passes;
import com.example.tfg.Modelo.Services;
import com.example.tfg.Modelo.User;
import com.example.tfg.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Clase donde esta el menu principal,
 * al acceder si hay internet sincroniza las bases de datos en las tablas de
 * reservas, pases y servicios
 */
public class MainScreen extends AppCompatActivity {
    //para evitar abrir bases de datos innecesarias ponemos sus managers en static
    public static BookingManager BM;
    public static ServiceManager SM;
    public static PassManager PM;
    //usaremos el ID del usuario para registrarle y consultar sus reservas
    public static int ID;
    private User u;
    private TextView welcome;
    private Button booking, mybookings, info, exit;
    private publishTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //iniciamos los managers de la BD interna
        BM = new BookingManager(this);
        SM = new ServiceManager(this);
        PM = new PassManager(this);

        //validamos el usuario que ha hecho login, el ID es static para pasarlo de activity más
        //comodamente
        ID = getIntent().getIntExtra("Id", -1);
        u = UM.getUser(ID);

        //Sincronizamos los elementos de la pantalla
        welcome = findViewById(R.id.txtWelcome);
        welcome.setText("Welcome " + u.getName());
        booking = findViewById(R.id.btnBooking);
        mybookings = findViewById(R.id.btnMyBookings);
        info = findViewById(R.id.btnInfo);
        exit = findViewById(R.id.btnExit);

        /**
         * Comprueba que haya conexion a internet para hacer una reserva y acceder a la activity
         */
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccessNetwork.checkNetworkState(view.getContext())) {
                    makeReserve(view);
                } else{
                    Toast("Se necesita conexion a internet para hacer una reserva");
                }
            }
        });

        /**
         * Se puede sin internet, para mirar las reservas del usuario y cancelarlas de ser necesario
         */
        mybookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myBookings(view);
            }
        });

        /**
         * Se puede sin internet, accede a la info de la app y del local.
         */

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessInfo();
            }
        });

        /**
         * Pregunta primero si quiere salir y el usuario elige
         */
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.Exit2)
                        .setMessage(R.string.QExit)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                exit();
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

        //Si hay internet sincroniza las bases en un hilo segundario
        if(AccessNetwork.checkNetworkState(this)) {
            mTask = new publishTask();
            mTask.execute();
        }

    }

    private void makeReserve(View view){
        Intent i = new Intent(this, Reserve.class);
        startActivity(i);
    }

    private void myBookings(View view){
        Intent i = new Intent(this, My_Bookings.class);
        startActivity(i);
    }

    private void accessInfo(){
        Intent i = new Intent(this, Info.class);
        startActivity(i);
    }

    private void exit(){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }
    //Hacemos la sincronizacion de las BDs en un hilo segundario para no congelar la app
    private class publishTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            fillReserves();
            fillPasses();
            fillServices();
            return null;
        }
    }

    //Sincroniza la tabla de reservas
    private void fillReserves(){
        Call<List<Booking>> call = apiManager.listBooking();

        call.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                List<Booking> lista = response.body();
                Log.d("D", response.body().toString());

                for(Booking b : lista){
                    Log.d("D", b.getId().toString());
                    if(!BM.CheckBooking(b.getId())) {
                        BM.RegisterBooking(b);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {

            }
        });
    }

    //Sincroniza la tabla de pases
    private void fillPasses(){
        Call<List<Passes>> callPass = apiManager.listPass();

        callPass.enqueue(new Callback<List<Passes>>() {
            @Override
            public void onResponse(Call<List<Passes>> call, Response<List<Passes>> response) {
                List<Passes> list = response.body();

                for(Passes p : list){
                    if(!PM.checkPass(p.getId())) {
                        PM.RegisterPass(p);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Passes>> call, Throwable t) {

            }
        });
    }

    //Sincroniza la tabla de servicios
    private void fillServices(){
        Call<List<Services>> callService = apiManager.listService();

        callService.enqueue(new Callback<List<Services>>() {
            @Override
            public void onResponse(Call<List<Services>> call, Response<List<Services>> response) {
                List<Services> list = response.body();

                for(Services s : list){
                    if(!SM.checkService(s.getId())){
                        SM.RegisterService(s);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Services>> call, Throwable t) {

            }
        });
    }

    public void Toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}