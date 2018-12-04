/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.cuestionario;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Métodos de ayuda relacionados con la solicitud y recepción de datos de preguntas del JSON.
 */
public final class Utilerias {

    /** Etiqueta para los mensajes de registro */
    private static final String LOG_TAG = Utilerias.class.getSimpleName();

    /**
     * Crear un constructor privado porque nadie debería crear un objeto {@link Utilerias}.
     * Esta clase solo debe contener variables y métodos estáticos, a los que se puede acceder
     * directamente desde el nombre de la clase Utilerias (y no se necesita una instancia de
     * objeto de Utilerias).
     */
    private Utilerias() {
    }

    /**
     * Consultar el conjunto de datos de JSON y devuelva una lista de objetos {@link Pregunta}
     */
    public static List<Pregunta> fetchPreguntaData(String requestUrl) {
        // Crear objeto URL
        URL url = createUrl(requestUrl);

        // Realizar una solicitud HTTP a la URL y reciba una respuesta JSON.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problema al hacer la solicitud HTTP.", e);
        }

        // Extraer los campos relevantes de la respuesta JSON y cree una lista de {@link Pregunta} s
        List<Pregunta> preguntas = extractFeatureFromJson(jsonResponse);

        // Regresar la lista de {@link Pregunta}s
        return preguntas;
    }

    /**
     * Devolver un nuevo objeto URL desde la URL de cadena dada.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problema al construir la URL ", e);
        }
        return url;
    }

    /**
     * Hacer una solicitud HTTP a la URL dada y devuelva una Cadena como respuesta.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // Si la URL es nula, regrese temprano.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Si la solicitud fue exitosa (código de respuesta 200), l
            // ea la secuencia de entrada y analice la respuesta.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Código de respuesta de error: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problema al recuperar los resultados del JSON preguntas.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Cerrar la secuencia de entrada podría generar una excepción IOException,
                // por lo que la firma del método makeHttpRequest (URL url) especifica que
                // se podría generar una excepción IOException.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convertir {@link InputStream} en una cadena que contenga toda la respuesta JSON del servidor.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Devolver una lista de los objetos {@link Pregunta} que se han creado al
     * analizar la respuesta JSON dada.
     */
    private static List<Pregunta> extractFeatureFromJson(String preguntaJSON) {
        // Si la cadena JSON está vacía o es nula, regrese antes.
        if (TextUtils.isEmpty(preguntaJSON)) {
            return null;
        }

        //Creamos una ArrayList vacía a la que podamos comenzar a agregar preguntas
        List<Pregunta> preguntas = new ArrayList<>();

        // Intenta analizar la cadena de respuesta JSON. Si hay un problema con la forma en
        // que se formatea el JSON, se lanzará un objeto de excepción JSONException. Capture
        // la excepción para que la aplicación no se bloquee e imprima el mensaje de error
        // en los registros.
        try {

            // Crear un objeto JSON desde la cadena de respuesta JSON
            JSONObject baseJsonResponse = new JSONObject(preguntaJSON);

            // Extraiga el JSONArray asociado con la clave llamada "arrayPreguntas", que
            // representa una lista de preguntas.
            JSONArray preguntaArray = baseJsonResponse.getJSONArray("arrayPreguntas");

            // Para cada pregunta en la preguntaArray, cree un objeto {@link Pregunta}
            for (int i = 0; i < preguntaArray.length(); i++) {

                // Obtén una sola pregunta en la posición i dentro de la lista de preguntas
                JSONObject currentPregunta = preguntaArray.getJSONObject(i);

                // Para una pregunta dada, extraiga el objeto JSON asociado con la clave llamada
                // "preguntas", que representa una lista de todas las propiedades para esa pregunta.
                JSONObject properties = currentPregunta.getJSONObject("preguntas");

                // Extrae el valor de la clave llamada "numeroPregunta"
                int numero = properties.getInt("numeroPregunta");

                // Extrae el valor de la clave llamada "pregunta"
                String contenido = properties.getString("pregunta");

                // Extraiga el JSONArray asociado con la clave llamada "respuestas", que
                // representa una lista de respuestas.
                JSONArray respuestaArray = properties.getJSONArray("respuestas");

                // Declarar un arreglo que almacena las respuestas
                String respuestas[] = new String[respuestaArray.length()];

                // Se recorre el respuestasArray para almacenar sus valores
                for (int j = 0; j < respuestaArray.length(); j++) {
                    respuestas[j] = (String) respuestaArray.get(j);
                }

                // Cree un nuevo objeto {@link Pregunta} con el numero, el contenido y las respuestas de la respuesta JSON.
                Pregunta pregunta = new Pregunta(numero, contenido, respuestas);

                // Agregue el nuevo {@link Pregunta} a la lista de preuntas.
                preguntas.add(pregunta);
            }

        } catch (JSONException e) {
            // Si se produce un error al ejecutar cualquiera de las declaraciones anteriores
            // en el bloque "try", captura la excepción aquí, para que la aplicación no se bloquee.
            // Imprima un mensaje de registro con el mensaje de la excepción.
            Log.e("Utilerias", "Problema al analizar los resultados del JSON Preguntas", e);
        }

        // Regresa la lista de preguntas
        return preguntas;
    }

}
