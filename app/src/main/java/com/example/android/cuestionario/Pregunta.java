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

/**
 * Un objeto {@link Pregunta} contiene información relacionada con una sola pregunta.
 */
public class Pregunta {

    /** Número de la pregunta */
    private int mNumero;

    /** Contenido de la pregunta */
    private String mContenido;

    /** Respuestas para la pregunta */
    private String mRespuestas[];


    /**
     * Construye un nuevo objeto {@link Pregunta}.
     *
     * @param numero es el numero de la pregunta
     * @param contenido es el contenido de la pregunta o formulación
     * @param respuestas son las opciones de la pregunta para ser contestada
     */
    public Pregunta(int numero, String contenido, String respuestas[]) {
        mNumero = numero;
        mContenido = contenido;
        mRespuestas = respuestas;
    }

    /**
     * Regresa el numero de la pregunta
     */
    public int getNumero() {
        return mNumero;
    }

    /**
     * Regresa el contenido de la pregunta
     */
    public String getContenido() {
        return mContenido;
    }

    /**
     * Regresa las respuestas correspondientes a la pregunta
     */
    public String[] getRespuestas(){
        return mRespuestas;
    }

}
