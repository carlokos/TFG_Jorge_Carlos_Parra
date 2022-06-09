package com.example.tfg.Pantallas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.tfg.R;

/**
 * Clase que contiene la info del local y documentacion de la app
 */
public class Info extends AppCompatActivity {

    TextView textHistory, textContact, textDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        textHistory = findViewById(R.id.textH);
        textContact = findViewById(R.id.textC);
        textDocument = findViewById(R.id.textD);

        textHistory.setText(fillHistory());
        textHistory.setMovementMethod(new ScrollingMovementMethod());

        textContact.setText(fillContact());
        textContact.setMovementMethod(new ScrollingMovementMethod());

        textDocument.setText(fillDocument());
        textDocument.setMovementMethod(new ScrollingMovementMethod());
    }

    public String fillHistory() {
        String h = "Las cuevas «Los Tarantos» se fundaron en 1972. Don José Martín Quesada y " +
                "Doña Concepción Maya compraron una cueva, en el barrio granadino del Sacromonte, " +
                "la cual se convertirá en la zambra flamenca «Cuevas Los Tarantos».";
        String h2 = "Nuestra cueva ha sido durante años escuela y trampolín para bailaores," +
                " guitarristas, y cantaores que han conseguido hacer exitosas carreras en el mundo " +
                "del flamenco, con reconocimiento nacional e internacional. Por nuestro escenario " +
                "han pasado los mejores artistas que ha dado Granada, en la guitarra, el baile " +
                "y el cante.";
        return h + "\n" + h2;
    }

    public String fillContact(){
        String c = "Telf: 687 975 608";
        String c1 = "Email: cuevaslostarantos@hotmail.com";
        String c2 = "Blog: https://cuevaslostarantos.com";
        return c + "\n" + c1 + "\n" + c2;
    }

    public String fillDocument(){
        String d = "Esta aplicación ha sido realizada por Jorge Carlos Parra Montoya como trabajo"+
                " final de grado, sin animo de lucro y sin uso real";
        String d1 = "Se ha realizado con Android studio y Laravel y funciona a traves de una api "+
                "que tambien he hecho yo, aquí los enlaces";
        String d2 = "Github app: https://github.com/carlokos/TFG_Jorge_Carlos_Parra" +
                "\n" +
                "Github api: https://github.com/carlokos/API_tfg_Jorge_Carlos_Parra.git";
        return d + "\n" + d1 + "\n" + d2;
    }
}