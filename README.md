# TFG_Jorge_Carlos_Parra
Trabajo de fin de grado de Jroge Carlos Parra, cuenta con las siguiente partes
<h3>Pantalla de Login</h3>
Una pantalla de Login de usuarios, se encargar de registrar usuarios en ambas Bases de datos (externa e interna) y de logear al usuario correctamente, además de 
mantener en todo momento las bases de datos con los mismos datos.
<h3>Menu principal</h3>
Un menú principal que sincroniza de nuevo ambas bases de datos, asegurandose que siempre esten con lo mismos datos. Además restringe ciertas partes en caso de no 
contar con conexion a internet
<h3>Pantalla de reserva</h3>
Una pantalla que solo se puede acceder con conexión a internet y sirve para hacer una reserva en el local de ejemplo, comprobando los datos y guardando dicha reserva
en ambas bases de datos
<h3>Pantalla de mis_reservas</h3>
Pantalla que muestra las reservas del usuario logeado en la app, funciona con un recycler view, se puede acceder sin conexión a esta pantalla pero si se quiere
cancelar una cita hay que tener conexión a internet
<h3>Pantalla de info</h3>
Pequeña pantalla que muestra la historia del local, contacto, y un pequeño disclaimer del tfg

En todo momento la app se encarga de que ambas bases de datos este sincronizadas y lo hace por una api que también he hecho yo y he subido a Github
