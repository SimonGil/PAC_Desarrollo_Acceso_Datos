package data_model;

/* Clase Data Model  para la entidad Libro
* 
* Esta clase se encarga de definir los atributos e implementar los métodos get y set de cada
* uno de ellos, para interactuar con el archivo de mapeo de Hibernate.
* También implementa un método toString() para mostrar información al usuario por consola.
* 
* @author Simon Gil
*/
public class Libro {
	//Atributos de la clase
	private int idLibro;
	private String titulo;
	private String autor;
	private int anoPublicacion;
	private boolean disponible;
	
	//Constructor vacío
	public Libro() {
		
	}
	//Getters y setters de la clase
	
	public int getIdLibro() {
		return idLibro;
	}
	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public int getAnoPublicacion() {
		return anoPublicacion;
	}
	public void setAnoPublicacion(int anoPublicacion) {
		this.anoPublicacion = anoPublicacion;
	}
	public boolean isDisponible() {
		return disponible;
	}
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
	//Método toString() para mostrar información de los libros por consola
	public String toString() {
		//Transformamos el valor booleano del atributo "disponible" en un String más comprensible
		String disponible = this.disponible ? "Sí" : "No";
		return "ID: " + this.idLibro + " | Título: " + this.titulo + " | Autor: " + this.autor + 
				" | Año de publicación: " + this.anoPublicacion + " | Disponible: " + disponible;
	}

}
