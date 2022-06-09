package com.example.tfg.Controlador;

import static com.example.tfg.Constants.apiManager;
import static com.example.tfg.Pantallas.MainScreen.BM;
import static com.example.tfg.Pantallas.MainScreen.PM;
import static com.example.tfg.Pantallas.MainScreen.SM;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.Modelo.Booking;
import com.example.tfg.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Nuestro adapter para el recycler view, esta hecho con el Objeto Booking
 */
public class Adapter extends RecyclerView.Adapter<Adapter.RecyclerHolder> {
    private java.util.List<Booking> List;

    public Adapter(List<Booking> list, Context context){
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
        Booking b = List.get(position);
        holder.getElements(b);
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        //declaramos los elementos del adapter
        ImageView icono;
        TextView servicio, fecha, precio;
        Button eliminar;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            //aqui relacionamos los elementos de nuestro "list-element" con las variables
            icono = itemView.findViewById(R.id.imgIcon);
            servicio = itemView.findViewById(R.id.titleService);
            fecha = itemView.findViewById(R.id.titleDate);
            precio = itemView.findViewById(R.id.titlePrice);
            eliminar = itemView.findViewById(R.id.btnCancel);
        }

        public void getElements(Booking b) {
            /**
             * Cada tarjeta muestra:
             * -El nombre de servicio (Ej: Espectáculo + copa)
             * -La fecha de la reserva y su hora
             * -El precio de la reserva
             */
            servicio.setText(SM.returnType(b.getService_Id()));
            fecha.setText(b.getSession_date() + " / " + PM.returnHour(b.getPass_Id()));
            precio.setText(String.valueOf(b.getPrice()) + "€");

            /**
             * Pregunta que si esta seguro que desea borrar la cita,
             * si acepta y hay conexion a internet entonces la cita es borrada
             */
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
                                            Toast("No se ha podido cancelar la cita, compruebe su conexión");
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
        }

        public void Toast(String msg) {
            Toast.makeText(itemView.getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}

