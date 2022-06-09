package com.example.tfg.Pantallas;

import static com.example.tfg.Pantallas.MainScreen.BM;
import static com.example.tfg.Pantallas.MainScreen.ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tfg.Controlador.Adapter;
import com.example.tfg.Controlador.BookingManager;
import com.example.tfg.Modelo.Booking;
import com.example.tfg.R;

import java.util.ArrayList;

/**
 * Clase que contiene un RecyclerView con las reservas de un usuario
 * solo mostrara las reservas del usuario que haya hecho login
 */
public class My_Bookings extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Booking> list;
    private Adapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        recyclerView = findViewById(R.id.rvList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        init();
    }

    /**
     * Busca las reservas de un usuario
     */
    public void init(){
        list = new ArrayList<Booking>();
        list = BM.listReserves(ID);
        Adapter = new Adapter(list, this);
        recyclerView.setAdapter(Adapter);
    }
}