package data_access_object;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import data_model.Lector;
import data_model.Libro;
import data_model.Prestamo;

/**
 * Clase Data Access Object para la entidad Prestamo
 * 
 * Esta clase se encarga de implementar los métodos necesarios para interactuar
 * con la base de datos cuando se trata de objetos tipo Prestamo.
 * 
 * @author Simon Gil
 */
public class PrestamoDAO extends EntidadDAO {
	/*
	 * Constructor de la clase.
	 * 
	 * Recibe un objeto Session.
	 */
	public PrestamoDAO(Session session) {
		super(session);
	}

	/*
	 * Método para insertar un préstamo.
	 * 
	 * Recibe instancias de Libro y Lector. Devuelve ID del prestamo insertado,
	 * o -1 en caso de error.
	 */
	public int insertarPrestamo(Libro libro, Lector lector) {
		Transaction tx = this.session.beginTransaction();
		int id;
		try {
			// Crear un nuevo objeto Prestamo
			Prestamo prestamo = new Prestamo();
			// Establecer el libro y el lector en el prestamo
			prestamo.setLibro(libro);
			prestamo.setLector(lector);
			// Guardamos el prestamo en la base de datos
			id = (int)this.session.save(prestamo);
			tx.commit();
			return id;
		} catch (Exception e) {
			tx.rollback();
			System.out.println("No ha sido posible insertar el prestamo.");
			e.printStackTrace();
			return -1;
		}
	}

	/*
	 * Método para eliminar un préstamo.
	 * 
	 * Recibe un número entero con el ID del préstamo a eliminar.
	 */
	public void borrarPrestamo(int idPrestamo) {
		Transaction tx = this.session.beginTransaction();
		try {
			Prestamo prestamo = session.get(Prestamo.class, idPrestamo);
			if (prestamo != null) {
				session.delete(prestamo);
				tx.commit();
				System.out.println("Prestamo con ID: " + idPrestamo+ " eliminado exitósamente.");
			} else {
				System.out.println("No se encontró ningún préstamo con el ID proporcionado.");
				tx.rollback();
			}
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("Error al eliminar el préstamo: " + e.getMessage());
		}
	}

	/*
	 * Método para obtener un préstamo.
	 * 
	 * Recibe un entero con el ID del préstamo que será seleccionado de la base de
	 * datos. Devuelve un objeto Prestamo con los valores de la selección.
	 */
	public Prestamo obtenerPrestamo(int idPrestamo) {
		Transaction tx = this.session.beginTransaction();
		Prestamo prestamo = null;
		try {
			prestamo = this.session.get(Prestamo.class, idPrestamo);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return prestamo;
	}

	/*
	 * Método para obtener una lista con todos los préstamos de la BD. 
	 * 
	 * Devuelve una estructura de datos List con todos los objetos prestamo.
	 */
	public List<Prestamo> obtenerPrestamos() {
		Transaction tx = this.session.beginTransaction();
		List<Prestamo> prestamos = null;
		try {
			Query<Prestamo> query = this.session.createQuery("FROM Prestamo", Prestamo.class);
			prestamos = query.getResultList();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}
		return prestamos;
	}

	/*
	 * Método para actualizar un préstamo.  
	 * 
	 * Actualiza el registro de la base de datos donde el ID
	 * sea igual a la propiedad idPrestamo del objeto, con los valores
	 * del resto de propiedades del mismo.
	 */
	public void actualizarPrestamo(Prestamo prestamo) {
		Transaction tx = this.session.beginTransaction();
		try {
			this.session.update(prestamo);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			System.out.println("Error en la actualización del préstamo: ");
			e.printStackTrace();
		}
	}

	/*
	 * Método para obtener el historial de préstamos de un lector.
	 * 
	 * Recibe el ID de un lector y devuelve una estructura List
	 * con todos los préstamos realizados al
	 * lector.
	 */
	public List<Prestamo> obtenerHistorialPrestamos(int idLector) {
		Transaction tx = this.session.beginTransaction();
		List<Prestamo> prestamos = null;
		try {
			Query<Prestamo> query = this.session
					.createQuery("SELECT p FROM Prestamo p WHERE p.lector.idLector = :idLector", Prestamo.class);
			query.setParameter("idLector", idLector);
			prestamos = query.getResultList();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return prestamos;
	}

	/*
	 * Método para obtener un préstamo no devuelto.
	 * 
	 * Recibe el id de un Libro y devuelve, si existe, el Prestamo
	 * no devuelto del mismo. 
	 * Se utiliza para la funcionalidad de devolución de préstamos.
	 * 
	 */
	public Prestamo obtenerPrestamoPorLibro(int idLibro) {
		Transaction tx = this.session.beginTransaction();
		Prestamo prestamo = null;
		try {
			Query<Prestamo> query = this.session.createQuery(
					"SELECT p FROM Prestamo p WHERE p.libro.idLibro = :idLibro AND p.fechaDevolucion = null",
					Prestamo.class);
			query.setParameter("idLibro", idLibro);
			prestamo = query.getSingleResult();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return prestamo;
	}

}
