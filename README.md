## PAC Desarrollo Acceso a Datos: Gestión de Biblioteca
Esta aplicación brinda la posibilidad de gestionar la base de datos de una biblioteca. Permite la inserción, eliminación,
actualización y obtención de información de Libros, Lectores (usuarios), y Préstamos.

## Estructura del proyecto
A modo de resumen o esquematización de la estructura del proyecto, podemos describir brevemente los tres packages que la conforman:

1: data_model
Este package contiene todas las clases que representan a las entidades de la aplicación (Libro, Lector y Prestamo). Cada una de ellas incluye los atributos oportunos para mapear los campos de sus respectivas tablas en la base de datos. Todas las clases del paquete tienen un constructor vacío, métodos Get y Set para cada uno de sus atributos, y un método toString() sobreescrito para mayor legibilidad de la información del objeto.

2: data_access_object
Este paquete contiene las clases DAO (Data Access Object) que implementan los métodos para comunicar las clases del modelo de datos con sus respectivas tablas en la base de datos. Es aquí donde se implementan los métodos CRUD. Todas las clases heredan de una clase abstracta EntidadDAO que tiene como atributo una Session de Hibernate.

3: app
Este último paquete contiene las clases que se encargan del funcionamiento general de la aplicación y la interacción con el usuario. Por una parte, la clase BibliotecaService, que implementa los métodos necesarios para fusionar las funcionalidades implementadas en las clases DAO con la capa de interacción con el usuario, liberando así de responsabilidades a la clase Main para que esta última sea más legible y tenga un código de menor extensión. Por la otra, la clase Main alberga el método main de entrada a la aplicación y la lógica de navegación entre las distintas opciones de los menús.


## Como ejecutar la aplicación

Paso 1:
Si no tenemos Maven instalado en nuestro PC, debemos instalarlo, de lo contrario podemos pasar al paso 2. Para instalar Maven nos dirigimos a https://maven.apache.org/download.cgi?. y descargamos el archivo zip binario apache-maven-x.x.x.bin.zip. A continuación, extraemos el contenido del zip en una carpeta adecuada, por ejemplo "Archivos de programa". Una vez extraído el contenido, tendremos que abrir la consola
(buscamos "cmd" en nuestro menú de inicio y ejecutamos). Para que el CMD reconozca Maven debemos configurar la variable de entorno introduciendo en consola el siguiente comando: setx PATH "%PATH%;directorio" asegurandonos de sustituir "directorio" por el directorio de
la carpeta "bin" de "apache-maven-3.9.6" (por ejemplo: C:\Program Files\apache-maven-3.9.6\bin). Debería recibir el siguiente mensaje:
"CORRECTO: se guardó el valor especificado". Podemos verificar que la configuración se haya realizado correctamente escribiendo en consola
el comando: "mvn --version".

Paso 2: 
Para que la aplicación pueda conectarse a una base de datos MySQL y funcionar adecuadamente, el método más sencillo es descargar XAMPP de lasiguiente direccion: https://www.apachefriends.org/es/download.html. Una vez instalado, ejecutamos el XAMPP Control Panel e iniciamos los módulos "Apache" y "MySQL" haciendo clic en sus respectivos botones "Start".
Paso 3: 
Con estos pasos realizados, abrimos de nuevo la consola y nos movemos a la carpeta del proyecto con el comando: "CD ruta_al_proyecto", asegurandonos de sustituir "ruta_al_proyecto" con la ubicación de este proyecto en su sistema. A continuación, introducimos el comando 
"mvn exec:java", iniciando la ejecución de la aplicación.

## Pruebas

El proyecto incluye pruebas unitarias para todos los métodos de las clases de acceso a datos (DAO). Estas clases se encargan de comunicar
el modelo de datos de la aplicación con la base de datos MySQL. Para ejecutar las pruebas y comprobar si los métodos CRUD de estas clases
se ejecutan correctamente, debemos escribir en consola el siguiente comando: "mvn test". Es importante asegurarse de ejecutar el comando
ubicándonos previamente en el directorio del proyecto, al igual que hicimos en el Paso 3 de la ejecución del programa. Una vez introducimos el comando anterior, se comenzarán a ejecutar todas las pruebas unitarias en tres bloques diferentes, uno por cada clase de acceso a datos (LectorDAO, LibroDAO y PrestamoDAO), y se nos informará por consola del resultado de los test realizados. 
El proyecto también incluye una clase de servicio "BibliotecaService" cuyos métodos, en su mayoría, requieren de la interacción del usuario, por lo que pueden ser probados siguiendo los pasos del siguiente apartado.

## Guía de uso y pruebas de métodos de interacción con el usuario.

La aplicación consta de un menú principal que nos permite elegir entre tres submenús diferentes: Gestionar Préstamos, Gestionar Libros, y Gestionar Lectores. La forma de manejarse por la aplicación consiste en ir introduciendo en consola el número de la opción que queramos seleccionar, salvo excepciones en la entrada de texto o fechas, donde el programa nos indicará qué debemos introducir en cada momento.
A continuación, se explica el funcionamiento de las opciones de cada uno de los submenús.

Menú 1- Gestionar Préstamos

Opción 1 - Nuevo préstamo
Cuando elegimos esta opción, se nos permite insertar un nuevo préstamo, o lo que es lo mismo "prestar" un libro a un lector. Por lo tanto, el programa nos mostrará una lista con todos los libros disponibles para préstamo, del cual debemos seleccionar uno introduciendo su ID. En caso de que no haya libros disponibles, el programa nos informará con el mensaje: "No se puede realizar un préstamo porque no hay libros disponibles". 
Una vez hayamos introducido el ID del libro que queremos prestar, se nos mostrará una lista con los lectores de la base de datos, y se nos pedirá que introduzcamos el ID de uno de ellos, tras lo cual, se insertará el préstamo en la base de datos con la fecha de préstamo actual. Como en el caso anterior, si no hay lectores en la base de datos, el programa nos informará y volverá al menú de Gestión de préstamos.

Opción 2 - Devolución de préstamo
Al elegir esta opción se nos mostrará una lista con los lectores guardados en la base de datos, y se nos pedirá que introduzcamos el ID del lector de la lista que va a realizar la devolución. En caso de que no haya ningún préstamo pendiente de devolución, el programa nos informará de ello. Tras introducir el ID del lector, se nos mostrarán los libros actualmente prestados a dicho lector y se nos pedirá que introduzcamos el ID del libro a devolver. Una vez introducidos el ID del lector y del libro que devolverá, se nos presenta la opción de introducir la fecha actual como fecha de devolución, o introducir una manualmente. En caso de elegir la segunda opción, tendremos que introducir una fecha en el formato indicado, de lo contrario nos volverá a pedir una fecha hasta que introduzcamos valores correctos. Por último, el programa nos informará de la devolución del préstamo con el mensaje: "El préstamo se ha devuelto correctamente".

Opción 3 - Borrar préstamo
Esta opción nos permite eliminar un préstamo de la base de datos. Cuando seleccionamos la opción, se nos presentará una lista con todos los préstamos de la base de datos, y se nos pedirá que introduzcamos el ID del préstamo que queremos borrar. Tras esto, el préstamo será eliminado. Destacar que cuando se elimina un préstamo, si este no tiene fecha de devolución, la disponibilidad del libro al que hace referencia el préstamo será modificada y pasará a estar disponible. Por otro lado, como en ocasiones anteriores, cuando no hay préstamos en la base de datos, el programa nos informará de ello y volverá al menú "Gestión de Préstamos". 

Opción 4 - Consultar historial de préstamos de un lector
En esta opción se nos mostrará una lista con todos los lectores de la base de datos, y se nos pedirá que introduzcamos el ID de uno de ellos para realizar la consulta. En caso de que no haya lectores en la base de datos, el programa nos informará de ello y volverá al menú "Gestión de préstamos". Tras introducir el ID, el programa nos mostrará una lista con el historial de préstamos del lector seleccionado.

Opción 5 - Actualizar información de un préstamo
En esta opción se nos mostrará una lista con todos los préstamos para que introduzcamos el ID de uno de ellos. Si no hay ningún préstamo en la base de datos el programa nos informará de ello y volverá al menú. Una vez introducimos el ID del préstamo, nos irá preguntando si queremos modificar cada uno de sus campos, indicando que debemos responder con una "Y" (Sí), o con una "N"(No). Si respondemos afirmativamente el programa nos indicará que introduzcamos el valor a actualizar en el campo seleccionado.
Al final del proceso, se nos indicará que el préstamo ha sido actualizado y se nos mostrarán los nuevos valores del mismo.

Menú 2 - Gestión de Libros

Opcion 1 - Insertar libro
Esta opción nos brindará la posibilidad de insertar un nuevo libro en la base de datos. Una vez seleccionada, nos irá preguntando los datos del libro (Título, Autor y Año de publicación). Cuando terminamos de introducir los valores, el libro será insertado en la base de datos. Destacar que el año de publicación debe ser un número entero, de lo contrario nos pedirá que volvamos a intentarlo.

Opcion 2 - Actualizar información de un libro
Esta opción es similar a la opción 5 del menú "Gestión de préstamos". Primero se nos muestra una lista con los libros guardados y se nos invita a introducir el ID de uno de ellos para modificarlo. A continuación iremos eligiendo que campos queremos modificar y cuales no. Cuando se nos pregunta si queremos modificar la disponibilidad del libro, se nos avisa de que puede dañar la integridad de la base de datos (podría llevar a errores en la gestión de préstamos) y que solo debemos hacerlo si tenemos plena seguridad en nuestra decisión. Finalmente el programa nos informa de que la actualización se ha llevado a cabo exitósamente y nos presenta los nuevos valores del libro actualizado.

Opcion 3 - Eliminar libro
Esta opción nos mostrará primeramente una lista con todos los libros de la base de datos para que elijamos uno de ellos. Esto lo haremos introduciendo el ID del libro seleccionado. A continuación, nos preguntará si estamos seguros de eliminar el libro ("Y":Sí, "N":No), y en caso afirmativo, lo eliminará. Como en otras funcionalidades de la aplicación, en caso de que no haya libros guardados en la base de datos, nos informará de que no es posible eliminar registros de una tabla vacía.

Opción 4 - Mostrar libros disponibles para préstamo
Cuando seleccionamos esta opción se nos muestra una lista con todos los libros que tenemos actualmente en la biblioteca, es decir, los que no están prestados. En caso de que la lista esté vacía, se nos mostrará un mensaje informando de ello.

Opción 5 - Mostrar todos los libros de la base de datos
Al seleccionar esta opción se nos mostrará una lista con todos los libros de la base de datos (tanto prestados como no prestados). En caso de que no haya libros guardados en la base de datos el programa nos informará de ello.

Menú 3 - Gestión de lectores

Opción 1 - Nuevo lector
Seleccionar esta opción nos permite insertar un nuevo lector en la base de datos. Para ello el programa nos irá preguntando los valores que queremos asignar a los diferentes campos de la tabla "lector". Tanto la lectura de datos del email como de la fecha de nacimiento del lector tienen métodos de validación que nos obligarán a volver a introducir los datos si estos no tienen un formato correcto. Una vez finalizamos con este proceso, el nuevo lector se insertará en la base de datos.

Opción 2 - Actualizar información de un lector
Al igual que con los métodos de actualización del resto de entidades, se nos irá preguntando si queremos modificar cada uno de los camos del lector, pidiéndonos que insertemos los nuevos valores de cada campo que hayamos decidido modificar. Una vez finaliza este proceso, el programa nos informará de que la actualización ha sido exitosa y nos mostrará los nuevos valores del lector.

Opción 3 - Eliminar lector
Esta opción nos permitirá eliminar un lector de la base de datos. Primero nos mostrará una lista con todos los lectores, pidiéndonos que insertemos el ID de uno de ellos. Una vez insertamos el ID, el programa nos preguntará si estamos seguros de querer eliminar al lector, y nos avisa de que la acción eliminará también todos sus préstamos (de esta forma, los préstamos no quedarán con una referencia vacía al lector). Si respondemos afirmativamente, el lector y todos sus préstamos serán eliminados de la base de datos. Destacar en este punto, que la eliminación de un préstamo no devuelto también marca como disponible el libro al que hace referencia, manteniendo la coherencia en nuestros datos.

Opción 4 - Consultar libros prestados a un lector
Al elegir esta opción se nos listarán todos los lectores de la base de datos, y el programa nos pedirá que introduzcamos el ID de uno de ellos para realizar la consulta. A continuación se nos mostrará una lista con todos los libros prestados al lector seleccionado. En caso de que esta lista esté vacía, el programa nos informará de ello.
