package data_access_object;



import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import data_model.Lector;
import data_model.Libro;
/**
 * Clase Data Access Object para la entidad Libro
 * 
 * Esta clase se encarga de implementar los métodos necesarios para interactuar con 
 * la base de datos cuando se trata de objetos tipo Libro.
 * 
 * @author Simon Gil
 */
public class LibroDAO extends EntidadDAO{
	/*
	 * Constructor de la clase.
	 * Recibe un objeto Session como parámetro.
	 */
	public LibroDAO(Session session) {
		super(session);
	}
	
	/*
	 * Método para insertar un Libro en la base de datos.
	 * 
	 * Recibe un objeto Libro como parámetro y lo inserta en la base de 
	 * datos MySQL. Devuelve el ID del libro o -1 si algo ha fallado en la inserción
	 */
	public int insertarLibro(Libro libro) {
		Transaction tx =  this.session.beginTransaction();
		try {
			int id = (int)this.session.save(libro);
			tx.commit();
			return id;
		}catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
			return -1;
		}
	}
	
	/*
	 * Método para eliminar un Libro de la base de datos.
	 * 
	 * Recibe un Integer que utiliza como ID de un Libro para eliminarlo 
	 * de su tabla en la base de datos.
	 * 
	 */
	public void borrarLibro(int idLibro) {
	      Transaction tx = this.session.beginTransaction();
	        try {
	            Libro libro = session.get(Libro.class, idLibro);
	            if (libro != null) {
	            	session.delete(libro);
	            	tx.commit();
	            	System.out.println("Libro con ID: " + idLibro + " eliminado exitósamente.");
	            }else {
	            	System.out.println("No se encontró ningún libro con el ID proporcionado.");
	            	tx.rollback();
	            }
	        } catch (HibernateException e) {
	        	if (tx != null) {
					tx.rollback();
				}
	        	System.out.println("Error al eliminar el libro:");
	        	e.printStackTrace();
	        }
	    }
	
	/*
	 * Método para actualizar un libro en la base de datos.
	 * 
	 * Recibe un objeto Libro y actualiza el registro cuya Primary Key 
	 * coincida con el atributo idLibro del objeto. Los valores de los atributos
	 * del objeto sustituirán los valores de los campos de la base de datos.
	 * 
	 */
	public void actualizarLibro(Libro libro) {
	       Transaction tx = this.session.beginTransaction();
	        try {
	            this.session.update(libro);
	            tx.commit();
	        } catch (Exception e) {
	        	 System.out.println("No ha sido posible modificar el libro con ID: " + libro.getIdLibro() + ".");
	            tx.rollback();
	            e.printStackTrace();
	        }
	}
	
	/*
	 * Método para obtener un Libro de la base de datos.
	 * 
	 * Recibe un Integer con el ID de registro en la tabla Libro, y
	 * realiza un select para obtenerlo.
	 * 
	 */
	public Libro obtenerLibro(int idLibro) {
		Transaction tx = this.session.beginTransaction();
        Libro libro = null;
        try {
            libro = this.session.get(Libro.class, idLibro);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            System.out.println("Error, no ha sido posible obtener un libro con el ID especificado: ");
            e.printStackTrace();
        }
        return libro;
	}
	
	/*
	 * Método para obtener una lista con los libros disponibles para préstamo
	 * 
	 * Devuelve una Lista con todos los Libros cuyo campo 
	 * "Disponible" tiene el valor "1" o true.
	 * 
	 */
	public List<Libro> obtenerLibrosDisponibles(){
	    Transaction tx = this.session.beginTransaction();
	    List<Libro> libros = null;
	    try {
	    	Query<Libro> query = this.session.createQuery("FROM Libro WHERE disponible = true", Libro.class);
	       libros = query.getResultList();
	       tx.commit();
	    }catch(Exception e) {
	    	tx.rollback();
	    	e.printStackTrace();
	    }
	    return libros;
	}

	/*
	 * Metodo para obtener una lista con todos los libros.
	 * 
	 * Selecciona todos los registro de la tabla Libro y los
	 * devuelve en una estructura List de tipo Libro.
	 * 
	 */
	public List<Libro> obtenerLibros(){
	    Transaction tx = this.session.beginTransaction();
	    List<Libro> libros = null;
		try{
			Query<Libro> query = this.session.createQuery("FROM Libro", Libro.class);
			libros = query.getResultList();
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
	       return libros;
	}
	/*
	 * Metodo para obtener los libros actualmente prestados a un lector.
	 * 
	 * Recibe el ID de un lector, y devuelve una lista con los objetos Libro 
	 * que no tienen fecha de devolución y han sido prestados al lector.
	 * 
	 */
	public List<Libro> librosActualmentePrestadosLector(int idLector){
		Transaction tx = this.session.beginTransaction();
		List<Libro> libros = null;
		try {
			Query<Libro> query = this.session.createQuery("SELECT p.libro FROM Prestamo p WHERE p.lector.idLector = :idLector AND p.fechaDevolucion = null", Libro.class);
			query.setParameter("idLector", idLector);
			libros = query.getResultList();
			tx.commit();
		}catch(Exception e) {
		e.printStackTrace();
		tx.rollback();
		}
		return libros;
	}
	
		
}


