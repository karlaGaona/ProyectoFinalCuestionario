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

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class PreguntaActivity extends AppCompatActivity {

    // Variables para la barra de progreso que se muestra al inicio
    private int progressStatus = 0;
    ProgressBar progressBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregunta_activity);
    }

    // EXTRA: Se agrego un ProgressBar para que muestre la carga del cuestionario, cuando termina de
    // cargarse, se utiliza Intent para ir a otra actividad, en este caso a Cuestionario para comenzar a
    // contestar
    public void cargar(View view) {
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 5;
                    // Modifica el ProgressBar y muestra
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    try {
                        // Se duerme por 100 milisegundos.
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent cuestionario = new Intent(getApplicationContext(), Cuestionario.class);
                startActivity(cuestionario);
            }
        }).start();

    }

}
