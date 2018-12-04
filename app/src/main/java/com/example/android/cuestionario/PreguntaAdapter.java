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

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Un {@link PreguntaAdapter} sabe cómo crear un diseño de elemento de lista para cada preguntas
 * en la fuente de datos (una lista de objetos {@link Pregunta}).
 *
 * Estos diseños de elementos de lista se proporcionarán a una vista de adaptador como
 * ListView para que se muestre al usuario.
 */
public class PreguntaAdapter extends ArrayAdapter<Pregunta> {

    protected List<Pregunta> preguntas;
    int listPosititon;

    // Contador de puntos
    int puntuacion = 0;
    // Lista donde se almancenan las preguntas correctas del usuario
    ArrayList preguntasCorrectas = new ArrayList();

    // Compruebe si una vista existente se está reutilizando, de lo contrario infle la vista
    ViewHolder viewHolder = null; // ver el caché de búsqueda almacenado en la etiqueta

    // ----------------------------------------------------------------------------------------
    // Variables utilizadas para elaborar el archivo de texto
    String n1, p1, r1p1, r2p1, r3p1, r4p1;
    String n2, p2, r1p2, r2p2, r3p2, r4p2;
    String n3, p3, r1p3, r2p3, r3p3, r4p3;
    String n4, p4;
    String n5, p5, r1p5, r2p5, r3p5, r4p5;
    String n6, p6, r1p6, r2p6, r3p6, r4p6;
    String n7, p7;
    String n8, p8, r1p8, r2p8, r3p8, r4p8;
    String n9, p9;
    String n10, p10, r1p10, r2p10, r3p10, r4p10;
    boolean correcto = true;

    // Variables para validación de radioButton, checkBox y editText
    boolean rg1 = false, rg3 = false, rg5 = false, rg8 = false, rg10 = false;
    boolean cbr1p2 = false, cbr2p2 = false, cbr3p2 = false, cbr4p2 = false;
    boolean cbr1p6 = false, cbr2p6 = false, cbr3p6 = false, cbr4p6 = false;
    boolean etr4 = false, etr7 = false, etr9 = false;

    ArrayList<String> selectedStrings2 = new ArrayList<String>();
    ArrayList<String> selectedStrings6 = new ArrayList<String>();

    // Variables para almacenar las respuestas correctas de cada pregunta
    String resCP1, resC1P2, resC2P2, resCP3, resCP4, resCP5, resC1P6, resC2P6, resCP7, resCP8, resCP9, resCP10;

    String texto1 = "", texto2Opc1 = "", texto2Opc2 = "", texto2Opc3 = "", texto2Opc4 = "", texto3="",
            texto4="", texto5="", texto6Opc1="", texto6Opc2="", texto6Opc3="", texto6Opc4="",
            texto7="", texto8="", texto9="", texto10="";
    // -------------------------------------------------------------------------------------------

    /**
     * Contructor nuevo de {@link PreguntaAdapter}.
     *
     * @param context de la aplicación
     * @param preguntas es la lsita de preguntas, cual es la fuente de datos del adaptador
     */
    public PreguntaAdapter(Context context, List<Pregunta> preguntas) {
        super(context, 0, preguntas);
        this.preguntas = preguntas;
    }

    /**
     * Devuelve una vista de elemento de lista que muestra información sobre la pregunta
     * en la posición dada en la lista de preguntas.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Compruebe si hay una vista de elemento de lista existente (denominada convertView)
        // que podamos reutilizar; de lo contrario, si convertView es nulo, infle un nuevo
        // diseño de elemento de lista.
        View listItemView = convertView;

        // Obtener el elemento de datos para esta posición
        Pregunta currentPregunta = getItem(position);

        if (listItemView == null) {
            // Si no hay una vista para reutilizar, infle una vista nueva para la fila
            viewHolder = new ViewHolder();

            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.pregunta_list_item, parent, false);

            // Encontrar el TextView con la vista numero de pregunta
            viewHolder.numeroView = (TextView) listItemView.findViewById(R.id.numero);
            // Establezca el color de fondo adecuado en el círculo de magnitud.
            // Recupera el fondo de TextView, que es un GradientDrawable.
            viewHolder.numeroCircle = (GradientDrawable) viewHolder.numeroView.getBackground();
            // Encontrar el TextView con la vista contenido de la pregunta
            viewHolder.contenidoView = (TextView) listItemView.findViewById(R.id.contenido);

            viewHolder.radioGroupP1 = (RadioGroup) listItemView.findViewById(R.id.rp1_radio_group);
            viewHolder.radioButtonR1P1 = (RadioButton) listItemView.findViewById(R.id.r1p1_radio_button);
            viewHolder.radioButtonR2P1 = (RadioButton) listItemView.findViewById(R.id.r2p1_radio_button);
            viewHolder.radioButtonR3P1 = (RadioButton) listItemView.findViewById(R.id.r3p1_radio_button);
            viewHolder.radioButtonR4P1 = (RadioButton) listItemView.findViewById(R.id.r4p1_radio_button);
            // Validar que radioButton del radioGroup ha sido seleccionado
            viewHolder.radioGroupP1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup mRadioGroup, int checkedId) {
                    for(int i=0; i<mRadioGroup.getChildCount(); i++) {
                        RadioButton btn = (RadioButton) mRadioGroup.getChildAt(i);

                        int t=mRadioGroup.getId();
                        System.out.println(t);

                        if(btn.getId() == checkedId) {
                            // Se obtiene el valor
                            texto1 = btn.getText().toString();
                            // do something with text
                            Log.i("Validar",texto1);
                            return;
                        }

                        rg1 = true;
                    }
                }
            });

            viewHolder.checkBoxR1P2 = (CheckBox) listItemView.findViewById(R.id.r1p2_checkbox);
            viewHolder.checkBoxR1P2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedStrings2.add(viewHolder.checkBoxR1P2.getText().toString());
                        texto2Opc1 = viewHolder.checkBoxR1P2.getText().toString() + "";
                        // do something with text
                        Log.i("Validar",texto2Opc1);
                        cbr1p2 = true;
                    }else{
                        selectedStrings2.remove(viewHolder.checkBoxR1P2.getText().toString());
                        cbr1p2 = false;
                    }
                }
            });
            viewHolder.checkBoxR2P2 = (CheckBox) listItemView.findViewById(R.id.r2p2_checkbox);
            viewHolder.checkBoxR2P2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedStrings2.add(viewHolder.checkBoxR2P2.getText().toString());
                        texto2Opc2 = viewHolder.checkBoxR2P2.getText().toString()+"";
                        // do something with text
                        Log.i("Validar",texto2Opc2);
                        cbr2p2 = true;
                    }else{
                        selectedStrings2.remove(viewHolder.checkBoxR2P2.getText().toString());
                        cbr2p2 = false;
                    }
                }
            });
            viewHolder.checkBoxR3P2 = (CheckBox) listItemView.findViewById(R.id.r3p2_checkbox);
            viewHolder.checkBoxR3P2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedStrings2.add(viewHolder.checkBoxR3P2.getText().toString());
                        texto2Opc3 = viewHolder.checkBoxR3P2.getText().toString()+"";
                        // do something with text
                        Log.i("Validar",texto2Opc3);
                        cbr3p2 = true;
                    }else{
                        selectedStrings2.remove(viewHolder.checkBoxR3P2.getText().toString());
                        cbr3p2 = false;
                    }
                }
            });
            viewHolder.checkBoxR4P2 = (CheckBox) listItemView.findViewById(R.id.r4p2_checkbox);
            viewHolder.checkBoxR4P2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedStrings2.add(viewHolder.checkBoxR4P2.getText().toString());
                        texto2Opc4 = viewHolder.checkBoxR4P2.getText().toString()+"";
                        // do something with text
                        Log.i("Validar",texto2Opc4);
                        cbr4p2 = true;
                    }else{
                        selectedStrings2.remove(viewHolder.checkBoxR4P2.getText().toString());
                        cbr4p2 = false;
                    }
                }
            });

            viewHolder.radioGroupP3 = (RadioGroup) listItemView.findViewById(R.id.rp3_radio_group);
            viewHolder.radioButtonR1P3 = (RadioButton) listItemView.findViewById(R.id.r1p3_radio_button);
            viewHolder.radioButtonR2P3 = (RadioButton) listItemView.findViewById(R.id.r2p3_radio_button);
            viewHolder.radioButtonR3P3 = (RadioButton) listItemView.findViewById(R.id.r3p3_radio_button);
            viewHolder.radioButtonR4P3 = (RadioButton) listItemView.findViewById(R.id.r4p3_radio_button);
            viewHolder.radioGroupP3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup mRadioGroup, int checkedId) {
                    for(int i=0; i<mRadioGroup.getChildCount(); i++) {
                        RadioButton btn = (RadioButton) mRadioGroup.getChildAt(i);
                        //int t=table.indexOfChild(table_row);
                        //System.out.println(t);
                        int t=mRadioGroup.getId();
                        System.out.println(t);

                        if(btn.getId() == checkedId) {
                            texto3 = btn.getText().toString();
                            // do something with text
                            Log.i("Validar",texto3);
                            return;
                        }

                        rg3 = true;
                    }
                }
            });

            viewHolder.editTextRP4 = (EditText) listItemView.findViewById(R.id.rp4_edit_text);
            viewHolder.editTextRP4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if(viewHolder.editTextRP4.getText().toString().trim().isEmpty()){
                            etr4 = false;
                        } else {
                            //Guarda valor en objeto
                            texto4 = viewHolder.editTextRP4.getText().toString()+"";
                            // do something with text
                            Log.i("Validar", texto4);
                            etr4 = true;
                        }
                    }
                }
            });

            viewHolder.radioGroupP5 = (RadioGroup) listItemView.findViewById(R.id.rp5_radio_group);
            viewHolder.radioButtonR1P5 = (RadioButton) listItemView.findViewById(R.id.r1p5_radio_button);
            viewHolder.radioButtonR2P5 = (RadioButton) listItemView.findViewById(R.id.r2p5_radio_button);
            viewHolder.radioButtonR3P5 = (RadioButton) listItemView.findViewById(R.id.r3p5_radio_button);
            viewHolder.radioButtonR4P5 = (RadioButton) listItemView.findViewById(R.id.r4p5_radio_button);
            // Validar que radioButton del radioGroup ha sido seleccionado
            viewHolder.radioGroupP5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup mRadioGroup, int checkedId) {
                    for(int i=0; i<mRadioGroup.getChildCount(); i++) {
                        RadioButton btn = (RadioButton) mRadioGroup.getChildAt(i);
                        //int t=table.indexOfChild(table_row);
                        //System.out.println(t);
                        int t=mRadioGroup.getId();
                        System.out.println(t);

                        if(btn.getId() == checkedId) {
                            texto5 = btn.getText().toString();
                            // do something with text
                            Log.i("Validar",texto5);
                            return;
                        }

                        rg5 = true;
                    }
                }
            });

            viewHolder.checkBoxR1P6 = (CheckBox) listItemView.findViewById(R.id.r1p6_checkbox);
            viewHolder.checkBoxR1P6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedStrings6.add(viewHolder.checkBoxR1P6.getText().toString());
                        texto6Opc1 = viewHolder.checkBoxR1P6.getText().toString()+"";
                        // do something with text
                        Log.i("Validar",texto6Opc1);
                        cbr1p6 = true;
                    }else{
                        selectedStrings6.remove(viewHolder.checkBoxR1P6.getText().toString());
                        cbr1p6 = false;
                    }
                }
            });
            viewHolder.checkBoxR2P6 = (CheckBox) listItemView.findViewById(R.id.r2p6_checkbox);
            viewHolder.checkBoxR2P6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedStrings6.add(viewHolder.checkBoxR2P6.getText().toString());
                        texto6Opc2 = viewHolder.checkBoxR2P6.getText().toString()+"";
                        // do something with text
                        Log.i("Validar",texto6Opc2);
                        cbr2p6 = true;
                    }else{
                        selectedStrings6.remove(viewHolder.checkBoxR2P6.getText().toString());
                        cbr2p6 = false;
                    }
                }
            });
            viewHolder.checkBoxR3P6 = (CheckBox) listItemView.findViewById(R.id.r3p6_checkbox);
            viewHolder.checkBoxR3P6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedStrings6.add(viewHolder.checkBoxR3P6.getText().toString());
                        texto6Opc3 = viewHolder.checkBoxR3P6.getText().toString()+"";
                        // do something with text
                        Log.i("Validar",texto6Opc3);
                        cbr3p6 = true;
                    }else{
                        selectedStrings6.remove(viewHolder.checkBoxR3P6.getText().toString());
                        cbr3p6 = false;
                    }
                }
            });
            viewHolder.checkBoxR4P6 = (CheckBox) listItemView.findViewById(R.id.r4p6_checkbox);
            viewHolder.checkBoxR4P6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedStrings6.add(viewHolder.checkBoxR4P6.getText().toString());
                        texto6Opc4 = viewHolder.checkBoxR4P6.getText().toString()+"";
                        // do something with text
                        Log.i("Validar",texto6Opc4);
                        cbr4p6 = true;
                    }else{
                        selectedStrings6.remove(viewHolder.checkBoxR4P6.getText().toString());
                        cbr4p6 = false;
                    }
                }
            });

            viewHolder.editTextRP7 = (EditText) listItemView.findViewById(R.id.rp7_edit_text);
            viewHolder.editTextRP7.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if(viewHolder.editTextRP7.getText().toString().trim().isEmpty()){
                            etr7 = false;
                        } else {
                            //Guarda valor en objeto
                            texto7 = viewHolder.editTextRP7.getText().toString()+"";
                            // do something with text
                            Log.i("Validar", texto7);
                            etr7 = true;
                        }
                    }
                }
            });

            viewHolder.radioGroupP8 = (RadioGroup) listItemView.findViewById(R.id.rp8_radio_group);
            viewHolder.radioButtonR1P8 = (RadioButton) listItemView.findViewById(R.id.r1p8_radio_button);
            viewHolder.radioButtonR2P8 = (RadioButton) listItemView.findViewById(R.id.r2p8_radio_button);
            viewHolder.radioButtonR3P8 = (RadioButton) listItemView.findViewById(R.id.r3p8_radio_button);
            viewHolder.radioButtonR4P8 = (RadioButton) listItemView.findViewById(R.id.r4p8_radio_button);
            viewHolder.radioGroupP8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup mRadioGroup, int checkedId) {
                    for(int i=0; i<mRadioGroup.getChildCount(); i++) {
                        RadioButton btn = (RadioButton) mRadioGroup.getChildAt(i);
                        //int t=table.indexOfChild(table_row);
                        //System.out.println(t);
                        int t=mRadioGroup.getId();
                        System.out.println(t);

                        if(btn.getId() == checkedId) {
                            texto8 = btn.getText().toString();
                            // do something with text
                            Log.i("Validar",texto8);
                            return;
                        }

                        rg8 = true;
                    }
                }
            });

            viewHolder.editTextRP9 = (EditText) listItemView.findViewById(R.id.rp9_edit_text);
            viewHolder.editTextRP9.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if(viewHolder.editTextRP9.getText().toString().trim().isEmpty()){
                            etr9 = false;
                        } else {
                            //Guarda valor en objeto
                            texto9 = viewHolder.editTextRP9.getText().toString()+"";
                            // do something with text
                            Log.i("Validar", texto9);
                            etr9 = true;
                        }
                    }
                }
            });

            viewHolder.radioGroupP10 = (RadioGroup) listItemView.findViewById(R.id.rp10_radio_group);
            viewHolder.radioButtonR1P10 = (RadioButton) listItemView.findViewById(R.id.r1p10_radio_button);
            viewHolder.radioButtonR2P10 = (RadioButton) listItemView.findViewById(R.id.r2p10_radio_button);
            viewHolder.radioButtonR3P10 = (RadioButton) listItemView.findViewById(R.id.r3p10_radio_button);
            viewHolder.radioButtonR4P10 = (RadioButton) listItemView.findViewById(R.id.r4p10_radio_button);
            viewHolder.radioGroupP10.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup mRadioGroup, int checkedId) {
                    for(int i=0; i<mRadioGroup.getChildCount(); i++) {
                        RadioButton btn = (RadioButton) mRadioGroup.getChildAt(i);
                        //int t=table.indexOfChild(table_row);
                        //System.out.println(t);
                        int t=mRadioGroup.getId();
                        System.out.println(t);

                        if(btn.getId() == checkedId) {
                            texto10 = btn.getText().toString();
                            // do something with text
                            Log.i("Validar",texto10);
                            return;
                        }

                        rg10 = true;
                    }
                }
            });

            // Almacena en caché el objeto viewHolder dentro de la vista nueva
            listItemView.setTag(viewHolder);
        } else {
            // La vista se está reciclando, recupere el objeto viewHolder de la etiqueta
            viewHolder = (ViewHolder) listItemView.getTag();
        }

            // Rellene los datos del objeto de datos a través del objeto viewHolder en la vista de plantilla

            String num = "" + currentPregunta.getNumero();
            // Muestra el numero de pregunta actual en ese TextView
            viewHolder.numeroView.setText(num);

            // Obtenga el color de fondo apropiado basado en el numero de pregunta actual
            int numeroColor = getNumeroColor(currentPregunta.getNumero());
            // Establecer el color en el círculo de numero.
            viewHolder.numeroCircle.setColor(numeroColor);

            // Mostrar el contenido de la pregunta actual en ese TextView
            viewHolder.contenidoView.setText(currentPregunta.getContenido());

            // Condición en la que de acuerdo al numero de posición se visualiza u ocultan los
            // componentes y se almacenan las respuestas a la pregunta actual para ser visualizada
            // en radio group, checkBox o editTex
            String respuestasArray[] = currentPregunta.getRespuestas();
            if (position == 0) {
                viewHolder.radioGroupP1.setVisibility(View.VISIBLE);
                viewHolder.radioButtonR1P1.setText(respuestasArray[0]);
                viewHolder.radioButtonR2P1.setText(respuestasArray[1]);
                viewHolder.radioButtonR3P1.setText(respuestasArray[2]);
                viewHolder.radioButtonR4P1.setText(respuestasArray[3]);
                resCP1 = respuestasArray[0];

                viewHolder.checkBoxR1P2.setVisibility(View.GONE);
                viewHolder.checkBoxR2P2.setVisibility(View.GONE);
                viewHolder.checkBoxR3P2.setVisibility(View.GONE);
                viewHolder.checkBoxR4P2.setVisibility(View.GONE);

                viewHolder.radioGroupP3.setVisibility(View.GONE);

                viewHolder.editTextRP4.setVisibility(View.GONE);

                viewHolder.radioGroupP5.setVisibility(View.GONE);

                viewHolder.checkBoxR1P6.setVisibility(View.GONE);
                viewHolder.checkBoxR2P6.setVisibility(View.GONE);
                viewHolder.checkBoxR3P6.setVisibility(View.GONE);
                viewHolder.checkBoxR4P6.setVisibility(View.GONE);

                viewHolder.editTextRP7.setVisibility(View.GONE);

                viewHolder.radioGroupP8.setVisibility(View.GONE);

                viewHolder.editTextRP9.setVisibility(View.GONE);

                viewHolder.radioGroupP10.setVisibility(View.GONE);

                n1 = ""+ currentPregunta.getNumero();
                p1 = currentPregunta.getContenido();
                r1p1 = respuestasArray[0];
                r2p1 = respuestasArray[1];
                r3p1 = respuestasArray[2];
                r4p1 = respuestasArray[3];
            } else if (position == 1) {
                viewHolder.radioGroupP1.setVisibility(View.GONE);

                viewHolder.checkBoxR1P2.setVisibility(View.VISIBLE);
                viewHolder.checkBoxR1P2.setText(respuestasArray[0]);
                viewHolder.checkBoxR2P2.setVisibility(View.VISIBLE);
                viewHolder.checkBoxR2P2.setText(respuestasArray[1]);
                viewHolder.checkBoxR3P2.setVisibility(View.VISIBLE);
                viewHolder.checkBoxR3P2.setText(respuestasArray[2]);
                viewHolder.checkBoxR4P2.setVisibility(View.VISIBLE);
                viewHolder.checkBoxR4P2.setText(respuestasArray[3]);
                resC1P2 = respuestasArray[1];
                resC2P2 = respuestasArray[2];

                viewHolder.radioGroupP3.setVisibility(View.GONE);

                viewHolder.editTextRP4.setVisibility(View.GONE);

                viewHolder.radioGroupP5.setVisibility(View.GONE);

                viewHolder.checkBoxR1P6.setVisibility(View.GONE);
                viewHolder.checkBoxR2P6.setVisibility(View.GONE);
                viewHolder.checkBoxR3P6.setVisibility(View.GONE);
                viewHolder.checkBoxR4P6.setVisibility(View.GONE);

                viewHolder.editTextRP7.setVisibility(View.GONE);

                viewHolder.radioGroupP8.setVisibility(View.GONE);

                viewHolder.editTextRP9.setVisibility(View.GONE);

                viewHolder.radioGroupP10.setVisibility(View.GONE);

                n2 = ""+ currentPregunta.getNumero();
                p2 = currentPregunta.getContenido();
                r1p2 = respuestasArray[0];
                r2p2 = respuestasArray[1];
                r3p2 = respuestasArray[2];
                r4p2 = respuestasArray[3];
            } else if (position == 2) {
                viewHolder.radioGroupP1.setVisibility(View.GONE);

                viewHolder.checkBoxR1P2.setVisibility(View.GONE);
                viewHolder.checkBoxR2P2.setVisibility(View.GONE);
                viewHolder.checkBoxR3P2.setVisibility(View.GONE);
                viewHolder.checkBoxR4P2.setVisibility(View.GONE);

                viewHolder.radioGroupP3.setVisibility(View.VISIBLE);
                viewHolder.radioButtonR1P3.setText(respuestasArray[0]);
                viewHolder.radioButtonR2P3.setText(respuestasArray[1]);
                viewHolder.radioButtonR3P3.setText(respuestasArray[2]);
                viewHolder.radioButtonR4P3.setText(respuestasArray[3]);
                resCP3 = respuestasArray[3];

                viewHolder.editTextRP4.setVisibility(View.GONE);

                viewHolder.radioGroupP5.setVisibility(View.GONE);

                viewHolder.checkBoxR1P6.setVisibility(View.GONE);
                viewHolder.checkBoxR2P6.setVisibility(View.GONE);
                viewHolder.checkBoxR3P6.setVisibility(View.GONE);
                viewHolder.checkBoxR4P6.setVisibility(View.GONE);

                viewHolder.editTextRP7.setVisibility(View.GONE);

                viewHolder.radioGroupP8.setVisibility(View.GONE);

                viewHolder.editTextRP9.setVisibility(View.GONE);

                viewHolder.radioGroupP10.setVisibility(View.GONE);

                n3 = ""+ currentPregunta.getNumero();
                p3 = currentPregunta.getContenido();
                r1p3 = respuestasArray[0];
                r2p3 = respuestasArray[1];
                r3p3 = respuestasArray[2];
                r4p3 = respuestasArray[3];
            } else if (position == 3) {
                viewHolder.radioGroupP1.setVisibility(View.GONE);

                viewHolder.checkBoxR1P2.setVisibility(View.GONE);
                viewHolder.checkBoxR2P2.setVisibility(View.GONE);
                viewHolder.checkBoxR3P2.setVisibility(View.GONE);
                viewHolder.checkBoxR4P2.setVisibility(View.GONE);

                viewHolder.radioGroupP3.setVisibility(View.GONE);

                viewHolder.editTextRP4.setVisibility(View.VISIBLE);
                viewHolder.editTextRP4.setContentDescription("Pregunta 4");
                resCP4 = "mayor latencia";

                viewHolder.radioGroupP5.setVisibility(View.GONE);

                viewHolder.checkBoxR1P6.setVisibility(View.GONE);
                viewHolder.checkBoxR2P6.setVisibility(View.GONE);
                viewHolder.checkBoxR3P6.setVisibility(View.GONE);
                viewHolder.checkBoxR4P6.setVisibility(View.GONE);

                viewHolder.editTextRP7.setVisibility(View.GONE);

                viewHolder.radioGroupP8.setVisibility(View.GONE);

                viewHolder.editTextRP9.setVisibility(View.GONE);

                viewHolder.radioGroupP10.setVisibility(View.GONE);

                n4 = ""+ currentPregunta.getNumero();
                p4 = currentPregunta.getContenido();
            } else if (position == 4) {
                viewHolder.radioGroupP1.setVisibility(View.GONE);

                viewHolder.checkBoxR1P2.setVisibility(View.GONE);
                viewHolder.checkBoxR2P2.setVisibility(View.GONE);
                viewHolder.checkBoxR3P2.setVisibility(View.GONE);
                viewHolder.checkBoxR4P2.setVisibility(View.GONE);

                viewHolder.radioGroupP3.setVisibility(View.GONE);

                viewHolder.editTextRP4.setVisibility(View.GONE);

                viewHolder.radioGroupP5.setVisibility(View.VISIBLE);
                viewHolder.radioButtonR1P5.setText(respuestasArray[0]);
                viewHolder.radioButtonR2P5.setText(respuestasArray[1]);
                viewHolder.radioButtonR3P5.setText(respuestasArray[2]);
                viewHolder.radioButtonR4P5.setText(respuestasArray[3]);
                resCP5 = respuestasArray[3];

                viewHolder.checkBoxR1P6.setVisibility(View.GONE);
                viewHolder.checkBoxR2P6.setVisibility(View.GONE);
                viewHolder.checkBoxR3P6.setVisibility(View.GONE);
                viewHolder.checkBoxR4P6.setVisibility(View.GONE);

                viewHolder.editTextRP7.setVisibility(View.GONE);

                viewHolder.radioGroupP8.setVisibility(View.GONE);

                viewHolder.editTextRP9.setVisibility(View.GONE);

                viewHolder.radioGroupP10.setVisibility(View.GONE);

                n5 = ""+ currentPregunta.getNumero();
                p5 = currentPregunta.getContenido();
                r1p5 = respuestasArray[0];
                r2p5 = respuestasArray[1];
                r3p5 = respuestasArray[2];
                r4p5 = respuestasArray[3];
            } else if (position == 5) {
                viewHolder.radioGroupP1.setVisibility(View.GONE);

                viewHolder.checkBoxR1P2.setVisibility(View.GONE);
                viewHolder.checkBoxR2P2.setVisibility(View.GONE);
                viewHolder.checkBoxR3P2.setVisibility(View.GONE);
                viewHolder.checkBoxR4P2.setVisibility(View.GONE);

                viewHolder.radioGroupP3.setVisibility(View.GONE);

                viewHolder.editTextRP4.setVisibility(View.GONE);

                viewHolder.radioGroupP5.setVisibility(View.GONE);

                viewHolder.checkBoxR1P6.setVisibility(View.VISIBLE);
                viewHolder.checkBoxR1P6.setText(respuestasArray[0]);
                viewHolder.checkBoxR2P6.setVisibility(View.VISIBLE);
                viewHolder.checkBoxR2P6.setText(respuestasArray[1]);
                viewHolder.checkBoxR3P6.setVisibility(View.VISIBLE);
                viewHolder.checkBoxR3P6.setText(respuestasArray[2]);
                viewHolder.checkBoxR4P6.setVisibility(View.VISIBLE);
                viewHolder.checkBoxR4P6.setText(respuestasArray[3]);
                resC1P6 = respuestasArray[1];
                resC2P6 = respuestasArray[2];

                viewHolder.editTextRP7.setVisibility(View.GONE);

                viewHolder.radioGroupP8.setVisibility(View.GONE);

                viewHolder.editTextRP9.setVisibility(View.GONE);

                viewHolder.radioGroupP10.setVisibility(View.GONE);

                n6 = ""+ currentPregunta.getNumero();
                p6 = currentPregunta.getContenido();
                r1p6 = respuestasArray[0];
                r2p6 = respuestasArray[1];
                r3p6 = respuestasArray[2];
                r4p6 = respuestasArray[3];
            } else if (position == 6) {
                viewHolder.radioGroupP1.setVisibility(View.GONE);

                viewHolder.checkBoxR1P2.setVisibility(View.GONE);
                viewHolder.checkBoxR2P2.setVisibility(View.GONE);
                viewHolder.checkBoxR3P2.setVisibility(View.GONE);
                viewHolder.checkBoxR4P2.setVisibility(View.GONE);

                viewHolder.radioGroupP3.setVisibility(View.GONE);

                viewHolder.editTextRP4.setVisibility(View.GONE);

                viewHolder.radioGroupP5.setVisibility(View.GONE);

                viewHolder.checkBoxR1P6.setVisibility(View.GONE);
                viewHolder.checkBoxR2P6.setVisibility(View.GONE);
                viewHolder.checkBoxR3P6.setVisibility(View.GONE);
                viewHolder.checkBoxR4P6.setVisibility(View.GONE);

                viewHolder.editTextRP7.setVisibility(View.VISIBLE);
                viewHolder.editTextRP7.setContentDescription("Pregunta 7");
                resCP7 = "DSL";

                viewHolder.radioGroupP8.setVisibility(View.GONE);

                viewHolder.editTextRP9.setVisibility(View.GONE);

                viewHolder.radioGroupP10.setVisibility(View.GONE);

                n7 = ""+ currentPregunta.getNumero();
                p7 = currentPregunta.getContenido();
            } else if (position == 7) {
                viewHolder.radioGroupP1.setVisibility(View.GONE);

                viewHolder.checkBoxR1P2.setVisibility(View.GONE);
                viewHolder.checkBoxR2P2.setVisibility(View.GONE);
                viewHolder.checkBoxR3P2.setVisibility(View.GONE);
                viewHolder.checkBoxR4P2.setVisibility(View.GONE);

                viewHolder.radioGroupP3.setVisibility(View.GONE);

                viewHolder.editTextRP4.setVisibility(View.GONE);

                viewHolder.radioGroupP5.setVisibility(View.GONE);

                viewHolder.checkBoxR1P6.setVisibility(View.GONE);
                viewHolder.checkBoxR2P6.setVisibility(View.GONE);
                viewHolder.checkBoxR3P6.setVisibility(View.GONE);
                viewHolder.checkBoxR4P6.setVisibility(View.GONE);

                viewHolder.editTextRP7.setVisibility(View.GONE);

                viewHolder.radioGroupP8.setVisibility(View.VISIBLE);
                viewHolder.radioButtonR1P8.setText(respuestasArray[0]);
                viewHolder.radioButtonR2P8.setText(respuestasArray[1]);
                viewHolder.radioButtonR3P8.setText(respuestasArray[2]);
                viewHolder.radioButtonR4P8.setText(respuestasArray[3]);
                resCP8 = respuestasArray[1];

                viewHolder.editTextRP9.setVisibility(View.GONE);

                viewHolder.radioGroupP10.setVisibility(View.GONE);

                n8 = ""+ currentPregunta.getNumero();
                p8 = currentPregunta.getContenido();
                r1p8 = respuestasArray[0];
                r2p8 = respuestasArray[1];
                r3p8 = respuestasArray[2];
                r4p8 = respuestasArray[3];
            } else if (position == 8) {
                viewHolder.radioGroupP1.setVisibility(View.GONE);

                viewHolder.checkBoxR1P2.setVisibility(View.GONE);
                viewHolder.checkBoxR2P2.setVisibility(View.GONE);
                viewHolder.checkBoxR3P2.setVisibility(View.GONE);
                viewHolder.checkBoxR4P2.setVisibility(View.GONE);

                viewHolder.radioGroupP3.setVisibility(View.GONE);

                viewHolder.editTextRP4.setVisibility(View.GONE);

                viewHolder.radioGroupP5.setVisibility(View.GONE);

                viewHolder.checkBoxR1P6.setVisibility(View.GONE);
                viewHolder.checkBoxR2P6.setVisibility(View.GONE);
                viewHolder.checkBoxR3P6.setVisibility(View.GONE);
                viewHolder.checkBoxR4P6.setVisibility(View.GONE);

                viewHolder.editTextRP7.setVisibility(View.GONE);

                viewHolder.radioGroupP8.setVisibility(View.GONE);

                viewHolder.editTextRP9.setVisibility(View.VISIBLE);
                viewHolder.editTextRP9.setContentDescription("Pregunta 9");
                resCP9 = "LLQ";

                viewHolder.radioGroupP10.setVisibility(View.GONE);

                n9 = ""+ currentPregunta.getNumero();
                p9 = currentPregunta.getContenido();
            } else if (position == 9) {
                viewHolder.radioGroupP1.setVisibility(View.GONE);

                viewHolder.checkBoxR1P2.setVisibility(View.GONE);
                viewHolder.checkBoxR2P2.setVisibility(View.GONE);
                viewHolder.checkBoxR3P2.setVisibility(View.GONE);
                viewHolder.checkBoxR4P2.setVisibility(View.GONE);

                viewHolder.radioGroupP3.setVisibility(View.GONE);

                viewHolder.editTextRP4.setVisibility(View.GONE);

                viewHolder.radioGroupP5.setVisibility(View.GONE);

                viewHolder.checkBoxR1P6.setVisibility(View.GONE);
                viewHolder.checkBoxR2P6.setVisibility(View.GONE);
                viewHolder.checkBoxR3P6.setVisibility(View.GONE);
                viewHolder.checkBoxR4P6.setVisibility(View.GONE);

                viewHolder.editTextRP7.setVisibility(View.GONE);

                viewHolder.radioGroupP8.setVisibility(View.GONE);

                viewHolder.editTextRP9.setVisibility(View.GONE);

                viewHolder.radioGroupP10.setVisibility(View.VISIBLE);
                viewHolder.radioButtonR1P10.setText(respuestasArray[0]);
                viewHolder.radioButtonR2P10.setText(respuestasArray[1]);
                viewHolder.radioButtonR3P10.setText(respuestasArray[2]);
                viewHolder.radioButtonR4P10.setText(respuestasArray[3]);
                resCP10 = respuestasArray[2];

                n10 = ""+ currentPregunta.getNumero();
                p10 = currentPregunta.getContenido();
                r1p10 = respuestasArray[0];
                r2p10 = respuestasArray[1];
                r3p10 = respuestasArray[2];
                r4p10 = respuestasArray[3];
            }

        /*--------------------------------------------------------------------------------------- */

        // Devuelve la vista completa para renderizar en pantalla
        return listItemView;
    }

    /**
     * Devuelva el color para el círculo de numero según el número de pregunta.
     *
     * @param numero de la pregunta
     */
    private int getNumeroColor(int numero) {
        int numeroColorResourceId;
        int numeroFloor = (int) Math.floor(numero);
        switch (numeroFloor) {
            case 0:
            case 1:
                numeroColorResourceId = R.color.numero1;
                break;
            case 2:
                numeroColorResourceId = R.color.numero2;
                break;
            case 3:
                numeroColorResourceId = R.color.numero3;
                break;
            case 4:
                numeroColorResourceId = R.color.numero4;
                break;
            case 5:
                numeroColorResourceId = R.color.numero5;
                break;
            case 6:
                numeroColorResourceId = R.color.numero6;
                break;
            case 7:
                numeroColorResourceId = R.color.numero7;
                break;
            case 8:
                numeroColorResourceId = R.color.numero8;
                break;
            case 9:
                numeroColorResourceId = R.color.numero9;
                break;
            default:
                numeroColorResourceId = R.color.numero10;
                break;
        }

        return ContextCompat.getColor(getContext(), numeroColorResourceId);
    }

    // Método que valida que todos los componentes tengan una respuesta para cada pregunta,
    // de lo contrario manda un mensaje de que faltan preguntas por responder
    public boolean validar(){
        if(!rg1 || !rg3 || !rg5 || !rg8 || !rg10
                || !etr4 || !etr7 || !etr9
                || (!cbr1p2 && !cbr2p2 && !cbr3p2 && !cbr4p2)
                || (!cbr1p6 && !cbr2p6 && !cbr3p6 && !cbr4p6)){
            String mensaje = " "+rg1+" "+rg3+" "+rg5+" "+rg8+" "+rg10+" "+etr4+" "+etr7+" "+etr9
                    +" "+cbr1p2+" "+cbr2p2+" "+cbr3p2+" "+cbr4p2+" "+cbr1p6+" "+cbr2p6+" "+cbr3p6+" "+cbr4p6;
            Log.i("","No ha sido validado"+mensaje);
            correcto = false;
        } else {
            String mensaje = " "+rg1+" "+rg3+" "+rg5+" "+rg8+" "+rg10+" "+etr4+" "+etr7+" "+etr9
                    +" "+cbr1p2+" "+cbr2p2+" "+cbr3p2+" "+cbr4p2+" "+cbr1p6+" "+cbr2p6+" "+cbr3p6+" "+cbr4p6;
            Log.i("","Ha sido validado "+mensaje);
            correcto = true;
        }

        return correcto;
    }

    // Método que analiza cada una de las preguntas con sus respectivas respuestas para obtener los
    // puntos obtenidos al contestarla y poder dar una puntuación al usuario, por lo que con cada
    // pregunta contestada correctamente se va sumando un punto y también se almacena la lista de
    // las preguntas correctas para mostrarlas al usuario al igual que su puntuación
    public int calificar() {
        // Pregunta 1
        if (resCP1.equals(texto1)) {
            puntuacion += 1;
            preguntasCorrectas.add("Pregunta 1");
        }

        // Pregunta 2
        if (resC1P2.equals(texto2Opc2) && resC2P2.equals(texto2Opc3)) {
            puntuacion += 1;
            preguntasCorrectas.add("Pregunta 2");
        }

        // Pregunta 3
        if (resCP3.equals(texto3)) {
            puntuacion += 1;
            preguntasCorrectas.add("Pregunta 3");
        }

        // Pregunta 4
        if (resCP4.equalsIgnoreCase(texto4)) {
            puntuacion += 1;
            preguntasCorrectas.add("Pregunta 4");
        }

        // Pregunta 5
        if (resCP5.equals(texto5)) {
            puntuacion += 1;
            preguntasCorrectas.add("Pregunta 5");
        }

        // Pregunta 6
        if (resC1P6.equals(texto6Opc2) && resC2P6.equals(texto6Opc3)) {
            puntuacion += 1;
            preguntasCorrectas.add("Pregunta 6");
        }

        // Pregunta 7
        if (resCP7.equalsIgnoreCase(texto7)) {
            puntuacion += 1;
            preguntasCorrectas.add("Pregunta 7");
        }

        // Pregunta 8
        if (resCP8.equals(texto8)) {
            puntuacion += 1;
            preguntasCorrectas.add("Pregunta 8");
        }

        // Pregunta 9
        if (resCP9.equalsIgnoreCase(texto9)) {
            puntuacion += 1;
            preguntasCorrectas.add("Pregunta 9");
        }

        // Pregunta 10
        if (resCP10.equals(texto10)) {
            puntuacion += 1;
            preguntasCorrectas.add("Pregunta 10");
        }

        Log.i("Calificación", ""+puntuacion);
        return puntuacion;
    }

    // Método que devuelve la lista de las preguntas correctas
    public ArrayList<String> guardar() {
        return preguntasCorrectas;
    }

    /**
     * El patrón de diseño de ViewHolder le permite acceder a cada vista de elemento de la lista
     * sin la necesidad de buscar, ahorrando valiosos ciclos de procesador. Específicamente, evita
     * las llamadas frecuentes de findViewById () durante el desplazamiento de ListView, y eso lo
     * hará más fácil.
     */
    private static class ViewHolder {
        TextView numeroView;
        TextView contenidoView;
        GradientDrawable numeroCircle;
        RadioGroup radioGroupP1;
        RadioButton radioButtonR1P1, radioButtonR2P1, radioButtonR3P1, radioButtonR4P1;
        RadioGroup radioGroupP3;
        RadioButton radioButtonR1P3, radioButtonR2P3, radioButtonR3P3, radioButtonR4P3;
        RadioGroup radioGroupP5;
        RadioButton radioButtonR1P5, radioButtonR2P5, radioButtonR3P5, radioButtonR4P5;
        RadioGroup radioGroupP8;
        RadioButton radioButtonR1P8, radioButtonR2P8, radioButtonR3P8, radioButtonR4P8;
        RadioGroup radioGroupP10;
        RadioButton radioButtonR1P10, radioButtonR2P10, radioButtonR3P10, radioButtonR4P10;
        EditText editTextRP4, editTextRP7, editTextRP9;
        CheckBox checkBoxR1P2, checkBoxR2P2, checkBoxR3P2, checkBoxR4P2;
        CheckBox checkBoxR1P6, checkBoxR2P6, checkBoxR3P6, checkBoxR4P6;
    }

}
