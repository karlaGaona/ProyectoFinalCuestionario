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

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Carga una lista de terremotos usando una AsyncTask para realizar la solicitud de red a la URL dada.
 */
public class PreguntaLoader extends AsyncTaskLoader<List<Pregunta>> {

    /** Etiqueta para los mensajes de registro */
    private static final String LOG_TAG = PreguntaLoader.class.getName();

    /** Consulta URL */
    private String mUrl;

    /**
     * Constructs a new {@link PreguntaLoader}.
     *
     * @param context de la actividad
     * @param url para cargar datos desde
     */
    public PreguntaLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Esto está en un hilo de fondo.
     */
    @Override
    public List<Pregunta> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Realice la solicitud de red, analice la respuesta y extraiga una lista de preguntas.
        List<Pregunta> preguntas = Utilerias.fetchPreguntaData(mUrl);
        return preguntas;
    }
}
