package com.example.android.cuestionario;

import android.Manifest;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Cuestionario extends AppCompatActivity implements LoaderCallbacks<List<Pregunta>>{

    // Contador de puntos
    int puntuacion = 0;
    // Lista donde se almancenan las preguntas correctas del usuario
    ArrayList preguntasCorrectas = new ArrayList();
    //Variables para que se pueda realizar el almacenamiento externo
    private static String ARCHIVO = Environment.getExternalStorageDirectory() + "/respuestas.txt";
    private Context context;

    private static final String LOG_TAG = PreguntaActivity.class.getName();

    /** URL de los datos del cuestionario (JSON) */
    private static final String url_datos =
            "https://api.myjson.com/bins/bf1n2";

    /**
     *
     * Valor constante para el ID del cargador de preguntas. Podemos elegir cualquier entero.
     * Esto realmente solo entra en juego si estás usando varios cargadores.
     */
    private static final int PREGUNTA_LOADER_ID = 1;

    /** Adaptador para la lista de preguntas */
    private PreguntaAdapter mAdapter;

    /** TextView que se despliega cuando la lista esta vacia */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Permisos para poder acceder a la memoria externa
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.
                WRITE_EXTERNAL_STORAGE}, 1);

        //Deja de lado FileProvider
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        // Encontrar una referencia a {@link ListView} en el layout
        ListView preguntaListView = (ListView) findViewById(R.id.list);

        // Referencia a el textview vacio
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        preguntaListView.setEmptyView(mEmptyStateTextView);

        // Crear un nuevo adaptador que tome una lista de preguntas vacia como entrada
        mAdapter = new PreguntaAdapter(this, new ArrayList<Pregunta>());

        // Poner el adaptador en el {@link ListView}
        // por lo que la lista se puede rellenar en la interfaz de usuario
        preguntaListView.setAdapter(mAdapter);

        // Obtener una referencia al ConnectivityManager para verificar el estado de la conectividad de la red
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Obtenga detalles sobre la red de datos predeterminada actualmente activa
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Si hay una conexión de red, busca datos
        if (networkInfo != null && networkInfo.isConnected()) {
            // Obtener una referencia al LoaderManager para interactuar con los cargadores.
            LoaderManager loaderManager = getLoaderManager();

            // Inicializa el cargador. Pase la constante int ID definida anteriormente y
            // pase el valor nulo para el paquete. Pase esta actividad para el parámetro
            // LoaderCallbacks (que es válido porque esta actividad implementa la interfaz LoaderCallbacks).
            loaderManager.initLoader(PREGUNTA_LOADER_ID, null, this);
        } else {
            //De lo contrario, muestre el error Primero,
            // oculte el indicador de carga para que aparezca un mensaje de error
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            //Actualizar estado vacío con mensaje de error de conexión
            mEmptyStateTextView.setText(R.string.no_conexion_internet);
        }
    }

    @Override
    public Loader<List<Pregunta>> onCreateLoader(int i, Bundle bundle) {
        //Crear un nuevo cargador para la URL dada
        return new PreguntaLoader(this, url_datos);
    }

    @Override
    public void onLoadFinished(Loader<List<Pregunta>> loader, List<Pregunta> preguntas) {
        //Ocultar el indicador de carga porque los datos han sido cargados.
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        //Establecer texto de estado vacío para mostrar "No se encontraron preguntas".
        mEmptyStateTextView.setText(R.string.no_preguntas);

        // Si hay una lista válida de {@link Pregunta}s, agréguelas al conjunto de datos
        // del adaptador. Esto activará el ListView para actualizar.
        if (preguntas != null && !preguntas.isEmpty()) {
            mAdapter.addAll(preguntas);
            //updateUi(preguntas);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Pregunta>> loader) {
        //Reinicio del cargador, para que podamos borrar nuestros datos existentes.
        mAdapter.clear();
    }

    // Método que permite la validación de las preguntas con sus opciones para RadioButton,
    // CheckBox, EditText para que no existan preguntas sin contestar, si esto ocurre se manda
    // un mensaje para indicarle al usuario que bede contestar todas las preguntas y si el
    // usuario contesto todas las preguntas entonces se procede a realizar los otros métodos
    public void procesar(View view) {
        if (mAdapter.validar()) {
            Log.i("", "Validación correcta");
            puntuacion = mAdapter.calificar();
            preguntasCorrectas = mAdapter.guardar();
            crearArchivo();
            cambiarVentana();
        } else {
            String mensaje = "Hay preguntas sin contestar";
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        }
    }

    // Método para abrir otra actividad por medio de Intent, en este caso se esta mandando una
    // varible también que será utilizada en la otro actividad atrevés de putExtra
    public void cambiarVentana() {
        Intent puntaje = new Intent(getApplicationContext(), Puntuacion.class);
        puntaje.putExtra("puntos", puntuacion);
        puntaje.putExtra("preguntasC", preguntasCorrectas);
        startActivity(puntaje);
    }

    // Método para crear un archivo txt que se almaneca en la memoria externa por lo que se necesitan
    // ciertos permisos para acceder a ella, estos son los que se declararon inicialmente, se revisa
    // que la maemoria este disponible para su utilización de lo contrario manda un mensaje de alerta
    public void crearArchivo() {
        //Verificamos el estado de la memoria SD.
        String estadoSD = Environment.getExternalStorageState();
        if (!estadoSD.equals(Environment.MEDIA_MOUNTED)) {
            //Si el estado es diferente a MEDIA_MOUNTED mostrarmos un mensaje.
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage("No se puede escribir en la memoria externa.");
            alert.setPositiveButton(android.R.string.ok, null);
            alert.show();
            return;
        }

        // Se crea el archivo txt ocn FileOutputStream indicandole que se sobreescribira cada que se
        // utilice la aplicación, en e archivo se nuestran las preguntas que se realizaron en el
        // las respuestas que el usuario dio y la respuesta que es la correcta, al igual que los
        // puntos o aciertos de la prueba
        try {
            FileOutputStream fos = new FileOutputStream(ARCHIVO, false);

            String texto = "DETALLES DE CUESTIONARIO \n \n"
                    + "Puntaje: " + puntuacion + "/10 \n \n"
                    + mAdapter.n1 +". "+ mAdapter.p1 + "\n"
                    + "\t" + "a) " + mAdapter.r1p1 + "\n"
                    + "\t" + "b) " + mAdapter.r2p1 + "\n"
                    + "\t" + "c) " + mAdapter.r3p1 + "\n"
                    + "\t" + "d) " + mAdapter.r4p1 + "\n" + "\n"
                    + "\t" + "TU RESPUESTA: " + mAdapter.texto1 + "\n"
                    + "\t" + "RESPUESTA CORRECTA: " + mAdapter.r1p1 + "\n" + "\n"
                    + mAdapter.n2 +". "+ mAdapter.p2 + "\n"
                    + "\t" + "a) " + mAdapter.r1p2 + "\n"
                    + "\t" + "b) " + mAdapter.r2p2 + "\n"
                    + "\t" + "c) " + mAdapter.r3p2 + "\n"
                    + "\t" + "d) " + mAdapter.r4p2 + "\n" + "\n"
                    + "\t" + "TU RESPUESTA: " + mAdapter.texto2Opc1 + "\t" + mAdapter.texto2Opc2 + "\t" + mAdapter.texto2Opc3 + "\t" + mAdapter.texto2Opc4 + "\n"
                    + "\t" + "RESPUESTA CORRECTA: " + mAdapter.r2p2 + ", " + mAdapter.r3p2 + "\n" + "\n"
                    + mAdapter.n3 +". "+ mAdapter.p3 + "\n"
                    + "\t" + "a) " + mAdapter.r1p3 + "\n"
                    + "\t" + "b) " + mAdapter.r2p3 + "\n"
                    + "\t" + "c) " + mAdapter.r3p3 + "\n"
                    + "\t" + "d) " + mAdapter.r4p3 + "\n" + "\n"
                    + "\t" + "TU RESPUESTA: " + mAdapter.texto3 + "\n"
                    + "\t" + "RESPUESTA CORRECTA: " + mAdapter.r4p3 + "\n" + "\n"
                    + mAdapter.n4 +". "+ mAdapter.p4 + "\n"
                    + "\t" + "TU RESPUESTA: " + mAdapter.texto4 + "\n"
                    + "\t" + "RESPUESTA CORRECTA: Mayor Latencia \n" + "\n"
                    + mAdapter.n5 +". "+ mAdapter.p5 + "\n"
                    + "\t" + "a) " + mAdapter.r1p5 + "\n"
                    + "\t" + "b) " + mAdapter.r2p5 + "\n"
                    + "\t" + "c) " + mAdapter.r3p5 + "\n"
                    + "\t" + "d) " + mAdapter.r4p5 + "\n" + "\n"
                    + "\t" + "TU RESPUESTA: " + mAdapter.texto5 + "\n"
                    + "\t" + "RESPUESTA CORRECTA: " + mAdapter.r4p5 + "\n" + "\n"
                    + mAdapter.n6 +". "+ mAdapter.p6 + "\n"
                    + "\t" + "a) " + mAdapter.r1p6 + "\n"
                    + "\t" + "b) " + mAdapter.r2p6 + "\n"
                    + "\t" + "c) " + mAdapter.r3p6 + "\n"
                    + "\t" + "d) " + mAdapter.r4p6 + "\n" + "\n"
                    + "\t" + "TU RESPUESTA: " + mAdapter.texto6Opc1 + "\t" + mAdapter.texto6Opc2 + "\t" + mAdapter.texto6Opc3 + "\t" + mAdapter.texto6Opc4 + "\n"
                    + "\t" + "RESPUESTA CORRECTA: " + mAdapter.r2p6 + ", " + mAdapter.r3p6 + "\n" + "\n"
                    + mAdapter.n7 +". "+ mAdapter.p7 + "\n"
                    + "\t" + "TU RESPUESTA: " + mAdapter.texto7 + "\n"
                    + "\t" + "RESPUESTA CORRECTA: DSL \n" + "\n"
                    + mAdapter.n8 +". "+ mAdapter.p8 + "\n"
                    + "\t" + "a) " + mAdapter.r1p8 + "\n"
                    + "\t" + "b) " + mAdapter.r2p8 + "\n"
                    + "\t" + "c) " + mAdapter.r3p8 + "\n"
                    + "\t" + "d) " + mAdapter.r4p8 + "\n" + "\n"
                    + "\t" + "TU RESPUESTA: " + mAdapter.texto8 + "\n"
                    + "\t" + "RESPUESTA CORRECTA: " + mAdapter.r2p8 + "\n" + "\n"
                    + mAdapter.n9 +". "+ mAdapter.p9 + "\n"
                    + "\t" + "TU RESPUESTA: " + mAdapter.texto9 + "\n"
                    + "\t" + "RESPUESTA CORRECTA: LLQ \n" + "\n"
                    + mAdapter.n10 +". "+ mAdapter.p10 + "\n"
                    + "\t" + "a) " + mAdapter.r1p10 + "\n"
                    + "\t" + "b) " + mAdapter.r2p10 + "\n"
                    + "\t" + "c) " + mAdapter.r3p10 + "\n"
                    + "\t" + "d) " + mAdapter.r4p10 + "\n" + "\n"
                    + "\t" + "TU RESPUESTA: " + mAdapter.texto10 + "\n"
                    + "\t" + "RESPUESTA CORRECTA: " + mAdapter.r3p10 + "\n" + "\n";
            fos.write(texto.getBytes());
            fos.close();
        } catch (Exception ex) {
            Log.e("Uso ListView", ex.getMessage(), ex);
        }
    }

}
