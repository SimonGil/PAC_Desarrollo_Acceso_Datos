package data_model;

import java.time.LocalDate;

/* Clase Data Model para la entidad Lector
* 
* Esta clase se encarga de definir los atributos e implementar los métodos get y set de cada
* uno de ellos, para interactuar con el archivo de mapeo de Hibernate.
* También implementa un método toString() para mostrar información al usuario por consola.
* 
* @author Simon Gil
*/
public class Lector {
	//Atributos de la clase
	private int idLector;
	private String nombre;
	private String apellidos;
	private String email;
	private LocalDate fechaNacimiento;
	//Constructor vacío
	public Lector() {}
	
	//Getters y setters
	public int getIdLector() {
		return idLector;
	}
	public void setIdLector(int idLector) {
		this.idLector = idLector;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	//Método toString() para mostrar lectores por consola, se comprueba que los valores no sean null para evitar errores en tiempo de ejecución
	public String toString() {
		String fechaDeNacimiento = this.fechaNacimiento == null ? "desconocida" : this.fechaNacimiento.toString();
		String nombre = this.nombre == null ? "desconocido" : this.nombre;
		String apellidos = this.apellidos == null ? "desconocidos" : this.apellidos;
		String email = this.email == null ? "desconocido" : this.email;
		return "ID: " + this.idLector + " | Nombre: " + nombre + " | Apellidos: " + apellidos +
				" | E-mail: " + this.email + " | Fecha de nacimiento: " + fechaDeNacimiento;
	}

}
