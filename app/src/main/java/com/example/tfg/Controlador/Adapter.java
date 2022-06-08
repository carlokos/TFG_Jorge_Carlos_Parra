package com.example.tfg.Controlador;

import static com.example.tfg.Constants.apiManager;
import static com.example.tfg.Pantallas.MainScreen.BM;
import static com.example.tfg.Pantallas.MainScreen.SM;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.Modelo.Bookings;
import com.example.tfg.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<Adapter.RecyclerHolder> {
    private java.util.List<Bookings> List;

    public Adapter(List<Bookings> list, Context context){
        this.List = list;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element,parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);

        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        //aqui mostramos nuestro contenido, le pasamos uno por uno los objetos de la lista
        Bookings b = List.get(position);
        holder.getElements(b);
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        //aqui relacionamos los elementos de nuestro "list-element" con las variables
        ImageView icono;
        TextView servicio, fecha, precio;
        Button eliminar, pagar;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);

            icono = itemView.findViewById(R.id.imgIcon);
            servicio = itemView.findViewById(R.id.titleService);
            fecha = itemView.findViewById(R.id.titleDate);
            precio = itemView.findViewById(R.id.titlePrice);
            eliminar = itemView.findViewById(R.id.btnCancel);
            pagar = itemView.findViewById(R.id.btnPay);
        }

        public void getElements(Bookings b) {
            servicio.setText(SM.returnType(b.getService_Id()));
            fecha.setText(b.getSession_date());
            precio.setText(String.valueOf(b.getPrice()) + "€");

            //listener de un boton
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder Alerta = new AlertDialog.Builder(itemView.getContext());
                    Alerta.setMessage("¿Desea cancelar esta cita?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Call<Void> call = apiManager.deleteReserve(b.getId());

                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            BM.removerReserve(b.getId());
                                            List.remove(b);
                                            notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast("SIN CONEXION A INTERNET, COMPRUEBE SU CONEXION");
                                        }
                                    });
                                }//aqui
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog titulo = Alerta.create();
                    titulo.setTitle("¿Cancelar Cita?");
                    titulo.show();
                }
            });

            pagar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast("EN TEORIA SE PAGA BIEN");
                }
            });
        }

        public void Toast(String msg) {
            Toast.makeText(itemView.getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}

