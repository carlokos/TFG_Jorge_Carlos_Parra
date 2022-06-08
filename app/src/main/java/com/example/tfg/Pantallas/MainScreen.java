package com.example.tfg.Pantallas;

import static com.example.tfg.Constants.apiManager;
import static com.example.tfg.Pantallas.Login.UM;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tfg.Controlador.AccessNetwork;
import com.example.tfg.Controlador.BookingManager;
import com.example.tfg.Controlador.PassManager;
import com.example.tfg.Controlador.ServiceManager;
import com.example.tfg.Controlador.UserManager;
import com.example.tfg.Modelo.Bookings;
import com.example.tfg.Modelo.Passes;
import com.example.tfg.Modelo.Services;
import com.example.tfg.Modelo.User;
import com.example.tfg.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreen extends AppCompatActivity {
    //para evitar abrir bases de datos innecesarias ponemos sus managers en static
    public static BookingManager BM;
    public static ServiceManager SM;
    public static PassManager PM;
    //usaremos el ID del usuario para registrarle y consultar sus reservas
    public static int ID;
    private User u;
    private TextView welcome;
    private Button booking, mybookings;
    private publishTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        BM = new BookingManager(this);
        SM = new ServiceManager(this);
        PM = new PassManager(this);

        ID = getIntent().getIntExtra("Id", -1);
        u = UM.getUser(ID);

        welcome = findViewById(R.id.txtWelcome);

        welcome.setText("Welcome " + u.getName());

        booking = findViewById(R.id.btnBooking);
        mybookings = findViewById(R.id.btnMyBookings);

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeReserve(view);
            }
        });

        mybookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myBookings(view);
            }
        });

        if(AccessNetwork.checkNetworkState(this)) {
            mTask = new publishTask();
            mTask.execute();
        }

        ActionBar actionBar = getSupportActionBar();
    }

    private void makeReserve(View view){
        Intent i = new Intent(this, Reserve.class);
        startActivity(i);
    }

    private void myBookings(View view){
        Intent i = new Intent(this, My_Bookings.class);
        startActivity(i);
    }

    private class publishTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Call<List<Bookings>> call = apiManager.listBooking();

            call.enqueue(new Callback<List<Bookings>>() {
                @Override
                public void onResponse(Call<List<Bookings>> call, Response<List<Bookings>> response) {
                    List<Bookings> lista = response.body();
                    Log.d("D", response.body().toString());

                    for(Bookings b : lista){
                        Log.d("D", b.getId().toString());
                        if(!BM.CheckBooking(b.getId())) {
                            BM.RegisterBooking(b);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Bookings>> call, Throwable t) {

                }
            });

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
            return null;
        }
    }
}