Proyecto. Aplicación que realice un cuestionario accediendo a la red.		Versión 1	04/12/2018

Descripción
-----------------------------------------------------------------------------------------------
Realización de una aplicación que realice un cuestionario de retroalimentación del curso de CCNA 4. La aplicación cuenta con una actividad principal que nuestra una bienvenida con un boton de comenzar que al presionar abre otra actividad en la que se encuentra el cuestionario el cual fue elaborado utilizando principalmente un listview en el que se cargan las preguntas y respuestas que se obtienen de un json que se subio a un servidor web para poder acceder a el en el momento que así se desee, también se utilizaron distintos componentes como TextView, ImageView, CheckBox, RadioGroup. RadioButton, EditText, ScrollView y sus propiedades, asi como la utiización de ViewGroup como LinearLayout. Despues de contestar el cuestionario, el cual no debe tener preguntas sin contestar, se presiona el boton de Calificar, la aplicación compara las repuestas del usuario con las respuestas correctas creandose un archivo txt con la retroalimentación del cuestionario el cual se almacena en memoria externa y hace una sumatoria de puntos obtenidos por respuestas correctas para dar una puntuación al usuario, al igual que una lista de preguntas correctas para que el usuario pueda observar su desempeño en general y en esa actividad se selecciona el botón de enviar para que la aplicación genere un correo electrónico en el que se adjunta el archivo txt de la retroalimentación que se genero y el usuario pueda ver más a detalle las preguntas y respuestas.
Se utilizo el patrón de diseño de ViewHolder que permite acceder a cada vista de elemento de la lista sin la necesidad de buscar, ahorrando valiosos ciclos de procesador. Específicamente, evita las llamadas frecuentes de findViewById () durante el desplazamiento de ListView, y eso lo hará más fácil.

-------------------------------------------------------------------------------------------------

Requisitos básicos para la instalación y utilización del programa
------------------------------------------------------------------------------------------------
- Para poder ejecutar el código se debe contar con el software Android Studio, que fue utilizado en esta práctica.
- La interfaz fue elaborada en el lenguaje XML.
- Se requiere la utilización de un emulador de Android Studio o un dispositivo móvil con sistema operativo android para realizar las pruebas correspondientes de la aplicación.
- Se requiere acceder a la siguiente liga que es donde se encuentra almanecado el JSON utilizado para el almacenamiento de preguntas y respuestas. https://api.myjson.com/bins/bf1n2
-------------------------------------------------------------------------------------------------

Contacto
-------------------------------------------------------------------------------------------------
Teléfono: 4773962137 
Correo: karla_gaona24@hotmail.com

Elaborado por: Karla Paola Gaona Delgado
