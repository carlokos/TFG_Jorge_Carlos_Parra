package com.example.tfg.Pantallas;

import static com.example.tfg.Pantallas.MainScreen.ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tfg.Controlador.Adapter;
import com.example.tfg.Controlador.BookingManager;
import com.example.tfg.Modelo.Bookings;
import com.example.tfg.R;

import java.util.ArrayList;
import java.util.List;

public class My_Bookings extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Bookings> list;
    private BookingManager BM;
    private Adapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        BM = new BookingManager(this);

        recyclerView = findViewById(R.id.rvList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        init();
    }

    public void init(){
        list = new ArrayList<Bookings>();
        list = BM.listReserves(ID);
        Adapter = new Adapter(list, this);
        recyclerView.setAdapter(Adapter);
    }
}