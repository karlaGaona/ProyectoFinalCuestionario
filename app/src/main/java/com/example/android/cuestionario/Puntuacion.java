package com.example.android.cuestionario;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Puntuacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);

        // Variable para la puntuación obtenida de la actividad anterior
        int p = getIntent().getExtras().getInt("puntos");

        // ArrayList que almacena las preguntas que fueron correctas
        ArrayList listaPC = getIntent().getExtras().getStringArrayList("preguntasC");

        // Imprime la puntuación obtenida en el cuestionario mostrandola en el TextView
        TextView puntos = (TextView) findViewById(R.id.puntaje_text_view);
        puntos.setText(p + "/10");

        // Se carga una lista obtenida para mostrar las preguntas que fueron correctas en un ListView
        ListView pCorrectas = (ListView) findViewById(R.id.listaPregCorrectas_text_view);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listaPC);
        pCorrectas.setAdapter(adaptador);
    }

    // Método que sirve para crear el email cargando el archivo adjunto que se creo y almeceno en la
    // memoria interna utilizando un Intent, el archivo es .txt
    public void guardar(View v) {
        // Obtiene la Uri del recurso que se encuentra en memoria externa.
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/respuestas.txt"));
        // Crea intent para enviar el email.
        Intent i = new Intent(Intent.ACTION_SEND);
        // Se adjunta el archivo indicamos que es un archivo txt
        i.setType("text/plain");
        // Agrega email con los datos correspondientes
        i.putExtra(Intent.EXTRA_SUBJECT, "Envio de Detalles de Cuestionario");
        i.putExtra(Intent.EXTRA_TEXT, "Hola te envío un archivo .txt con los detalles del cuestionario");
        i.putExtra(Intent.EXTRA_STREAM,  uri);
        startActivity(Intent.createChooser(i, "Enviar e-mail mediante:"));
    }
}
