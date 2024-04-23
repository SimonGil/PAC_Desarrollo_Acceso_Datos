package data_model;

import java.time.LocalDate;
/* Clase Data Model  para la entidad Prestamo
* 
* Esta clase se encarga de definir los atributos e implementar los métodos get y set de cada
* uno de ellos, para interactuar con el archivo de mapeo de Hibernate.
* También implementa un método toString() para mostrar información al usuario por consola.
* 
* @author Simon Gil
*/
public class Prestamo {
	//Atributos de la clase
	private int idPrestamo;
	private LocalDate fechaPrestamo;
	private LocalDate fechaDevolucion;
	private Libro libro;
	private Lector lector;
    
	//Constructor vacío
	public Prestamo() {
		this.fechaPrestamo = LocalDate.now();
	}
	
   //Constructor con Libro y Lector como parámetros
	public Prestamo(Libro libro, Lector lector) {
		this.libro = libro;
		this.lector = lector;
		//La fecha del prestamo será la fecha de creación del objeto
		this.fechaPrestamo = LocalDate.now();
	}
	
	//Getters y Setters
	public int getIdPrestamo() {
		return idPrestamo;
	}
	public void setIdPrestamo(int idPrestamo) {
		this.idPrestamo = idPrestamo;
	}
	public LocalDate getFechaPrestamo() {
		return fechaPrestamo;
	}
	public void setFechaPrestamo(LocalDate fechaPrestamo) {
		this.fechaPrestamo = fechaPrestamo;
	}
	public LocalDate getFechaDevolucion() {
		return fechaDevolucion;
	}
	public void setFechaDevolucion(LocalDate fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}
	public Libro getLibro() {
		return libro;
	}
	public void setLibro(Libro libro) {
		this.libro = libro;
	}
	public Lector getLector() {
		return lector;
	}
	public void setLector(Lector lector) {
		this.lector = lector;
	}
	
	//Método toString() para mostrar prestamos por consola
	public String toString() {
		//Evaluamos si el libro ha sido devuelto (fechaDevolucion != null). Si ha sido devuelto mostramos fecha, si no, informamos de ello
		String devolucion = this.fechaDevolucion != null? this.fechaDevolucion.toString() : "No ha sido devuelto";
		return "ID: " + this.idPrestamo + " | Fecha de préstamo: " + this.fechaPrestamo.toString() + 
				" | Fecha de devolución: " + devolucion + " | ID Libro: " + this.libro.getIdLibro() + " | ID Lector: " + this.lector.getIdLector();
	}
}
