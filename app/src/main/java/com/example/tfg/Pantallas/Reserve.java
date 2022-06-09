package com.example.tfg.Pantallas;

import static com.example.tfg.Constants.apiManager;
import static com.example.tfg.Pantallas.MainScreen.ID;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.Controlador.AccessNetwork;
import com.example.tfg.Modelo.Booking;
import com.example.tfg.Modelo.Passes;
import com.example.tfg.Modelo.Services;
import com.example.tfg.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Se encarga de hacer una nueva reserva, para esto le pasamos el ID del usuario conectado
 * para esto se requerira conexion a internet y no se podra acceder a esta pantalla sin internet
 */
public class Reserve extends AppCompatActivity {
    private Spinner spinner1, spinner2;
    private TextView tvDate, tvPrice;
    private EditText txtNumP;
    private Button btnReserve;
    private publishTask mTask;
    private int year, month, day;
    /**
     * La api necesita un formato de fecha en concreto asi que para asegurarnos de mostrar
     * la fecha correctamente al usuario y que no haya ningun error a la hora de enviar los datos
     * usamos una fecha auxiliar
     */
    private String dateaux;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        //Sincronizamos los elementos de la pantalla
        spinner1 = findViewById(R.id.spinnerHours);
        spinner2 = findViewById(R.id.spinnerServices);
        tvDate = findViewById(R.id.txtFecha);
        tvPrice = findViewById(R.id.txtPrice);
        txtNumP = findViewById(R.id.txtNumPeople);
        btnReserve = findViewById(R.id.btnConfirm);

        txtNumP.setText("1");
        /**
         * Cada vez que se modifique el numero de personas se calcula el precio
         */
        txtNumP.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                CalculatePrice();
                return false;
            }
        });
        /**
         * Cada vez que se modifique el servicio calcula el precio con el nuevo precio
         */
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CalculatePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //ponermos que por defecto este la fecha de mañana para evitar errores
        tvDate.setText(fillDate());

        //en el hilo segundario colocamos los pases y los servicios de la base de datos externa
        mTask = new publishTask();
        mTask.execute();
        //asignamos el DatePicker para escoger la fecha
        DatePicker(tvDate);

        /**
         * Da una confirmación de la reserva y, si tiene conexion a internet, la guarda en ambas bases
         * de datos y vuelve al menu principal
         */
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (validateDate() && !txtNumP.getText().toString().equals("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle(R.string.ConfirmR)
                                .setMessage(getDialogText())
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(AccessNetwork.checkNetworkState(view.getContext())) {
                                            makeReserve();
                                        }
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        Toast("No se ha guardado la reserva");
                                    }
                                });
                                builder.show();
                                //El numero de personas se puede bugear, hay que asegurarse que no sea 0 o menor
                    } else if (txtNumP.getText().toString().equals("0") || txtNumP.getText().toString().isEmpty()) {
                        txtNumP.setText("1");
                    } else {
                        Toast("No se puede reservar en la fecha introducida");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Rellena las horas de los pases en el primer spinner
    private void fillHours() {
        Call<List<Passes>> call = apiManager.listPass();

        call.enqueue(new Callback<List<Passes>>() {
            @Override
            public void onResponse(Call<List<Passes>> call, Response<List<Passes>> response) {
                List<Passes> pases = response.body();
                ArrayAdapter<Passes> adapter = new ArrayAdapter<Passes>(Reserve.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, pases);

                spinner1.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Passes>> call, Throwable t) {

            }
        });
    }

    //Rellena los servicios en el segundo spinner
    private void fillServices() {
        Call<List<Services>> call = apiManager.listService();

        call.enqueue(new Callback<List<Services>>() {
            @Override
            public void onResponse(Call<List<Services>> call, Response<List<Services>> response) {
                List<Services> services = response.body();

                ArrayAdapter<Services> adapter = new ArrayAdapter<Services>(Reserve.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, services);

                spinner2.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Services>> call, Throwable t) {

            }
        });
    }

    /**
     * Coge la fecha de mañana y la guarda en la fecha auxiliar y la coloca en pantalla automaticamente
     * @return String
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String fillDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
        String year = dtf.format(now);
        dtf = DateTimeFormatter.ofPattern("MM");
        String month = dtf.format(now);
        dtf = DateTimeFormatter.ofPattern("dd");
        String day = dtf.format(now);
        day = String.valueOf(Integer.valueOf(day) + 1);
        String date = day + "/" + month + "/" + year;
        dateaux = year + "-" + month + "-" + day;
        return date;
    }

    private void CalculatePrice() {
        Services s = (Services) spinner2.getSelectedItem();
        int num;
        if (txtNumP.getText().toString().isEmpty() || txtNumP.getText().toString().equals("0")) {
            txtNumP.setText("1");
        }
        num = Integer.parseInt(txtNumP.getText().toString());
        Double price = s.getPrice() * num;
        tvPrice.setText(Double.toString(price));
    }

    /**
     * Metodo que le asignamos a un textview para que al hacerle click nos deje elegir
     * la fecha, ponemos la fecha escogida por el usuario en pantalla y la guardamos en el
     * formato que la api necesita en la fecha auxiliar
     * @param dt
     */
    private void DatePicker(TextView dt) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Reserve.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        dateaux = year + "-" + month + "-" + day;
                        dt.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    //Relenamos los spinners en un hilo segundario para evitar congelamientos
    private class publishTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            fillServices();
            fillHours();
            return null;
        }
    }

    /**
     * Metodo que valida la fecha, asegurandose que no sea una fecha inferior a la de mañana,
     * para eso cogemos la fecha de mañana y la fecha que ha escogido el usuario y la restamos.
     * Pasamos el resultado el dia y si es igual o mayor a 1 esta validada
     * @return boolean
     * @throws ParseException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validateDate() throws ParseException {
        boolean correct = false;
        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);
        //El mes lo coge mal asi que hay que sumarle uno para los calculos sean correctos
        String date1 = d + "/" + String.valueOf(m + 1) + "/" + y;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha1 = format.parse(date1);
        Date fecha2 = format.parse(tvDate.getText().toString());
        //aqui esta en milisegundos
        long diff = fecha2.getTime() - fecha1.getTime();
        TimeUnit time = TimeUnit.DAYS;
        long dif = time.convert(diff, TimeUnit.MILLISECONDS);
        if (dif >= 1) {
            correct = true;
        }
        return correct;
    }

    /**
     * Guarda la reserva en la base de datos externa y fuerza la sincronizacion con la BD interna
     * volviendo a la pantalla principal
     */
    private void makeReserve() {
        Passes p = (Passes) spinner1.getSelectedItem();
        Services s = (Services) spinner2.getSelectedItem();

        Booking b = new Booking();

        b.setPass_Id(p.getId());
        b.setSession_date(dateaux);
        b.setNum_people(Integer.parseInt(txtNumP.getText().toString()));
        b.setPrice(Double.parseDouble(tvPrice.getText().toString()));
        b.setPrepaid('N');
        b.setUser_Id(ID);
        b.setService_Id(s.getId());

        Call<Booking> call = apiManager.postBooking(b);

        Log.d("RESERVA", b.toString());

        /**
         * La razon por la que onResponse es donde controlamos la excepcion es porque
         * cuando da error el body del response no esta nulo (ya que devuelve el fallo) y retrofit
         * lo situa en ese sitio al no ser nulo, mientras que ha postear algo con exito el body
         * del response es nulo, asi que lo situa en onFailure
         */
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                Toast("Esta reserva ya esta hecha");
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Intent i = new Intent(Reserve.this, MainScreen.class);
                i.putExtra("Id", ID);
                startActivity(i);
                finish();
                Toast("Reserva guardada correctamente");
            }
        });
    }

    public void Toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //Mostramos la reserva que va a hacer antes de confirmarla
    private String getDialogText() {
        String date = tvDate.getText().toString();
        String num = txtNumP.getText().toString();
        String hour = spinner1.getSelectedItem().toString();
        String service = spinner2.getSelectedItem().toString();

        return "Fecha: " + date + "\n" +
                "Personas: " + num + "\n" +
                "Hora: " + hour + "\n" +
                "Reserva: " + service;
    }
}