package data_access_object;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import data_model.Lector;

/**
 * Clase Data Access Object para la entidad Lector
 * 
 * Esta clase se encarga de implementar los métodos necesarios para interactuar con 
 * la base de datos cuando se trata de objetos tipo Lector.
 * 
 * @author Simon Gil
 */
public class LectorDAO extends EntidadDAO {
	/*
	 * Constructor de la clase.
	 * 
	 * Recibe un objeto Session como parámetro
	 */
	public LectorDAO(Session session) {
		super(session);
	}
	
	/*
	 * Método para insertar lector.
	 * 
	 * Recibe un objeto Lector y e inserta sus valores en la base de datos.
	 * Devuelve el ID asignado, lo que nos será útil en las pruebas o -1 en caso 
	 * de error.
	 * 
	 */
	public int insertarLector(Lector lector) {
		Transaction tx =  this.session.beginTransaction();
		try {
			int id = (int) this.session.save(lector);
			tx.commit();
			return id;
		}catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
			return -1;
		}
	}
	/*
	 * Método para eliminar un Lector
	 * 
	 * Recibe el ID de un Lector y lo elimina de la base de datos.
	 */
	public void borrarLector(int idLector) {
		 Transaction tx = this.session.beginTransaction();
	        try {
	            Lector lector = session.get(Lector.class, idLector);
	            if(lector != null) {
	            session.delete(lector);
	            tx.commit();
	            System.out.println("Lector con ID: " + idLector + " eliminado exitósamente.");
	            }else {
	            	System.out.println("No se ha encontradó ningún lector con el ID proporcionado.");
	            }
	        } catch (HibernateException e) {
				if (tx != null) {
					tx.rollback();
				}
	            System.out.println("Error al eliminar el lector: " + e.getMessage());
	        }
	}
	
	/*
	 * Método para actualizar un lector en la base de datos.
	 * 
	 * Recibe un objeto Lector y actualiza el registro cuya Primary Key 
	 * coincida con el atributo idLector del objeto. Los valores de los atributos
	 * del objeto sustituirán los valores de los campos de la base de datos.
	 * 
	 */
	public void actualizarLector(Lector lector) {
		 Transaction tx = this.session.beginTransaction();
	        try {
	            this.session.update(lector);
	            tx.commit();
	        } catch (Exception e) {
	            tx.rollback();
	            System.out.println("Error al modificar el lector:");
	            e.printStackTrace();
	        }
	}
	
	/*
	 * Método para obtener un lector de la base de datos.
	 * 
	 * Recibe como parámetro el ID de un lector y devuelve el
	 * objeto Lector tras realizar un SELECT en la base de datos.
	 */
	public Lector obtenerLector(int idLector) {
		Transaction tx = this.session.beginTransaction();
        Lector lector = null;
        try {
            lector = this.session.get(Lector.class, idLector);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return lector;
	}

	/*
	 * Método para obtener una lista con todos los lectores de la BD.
	 * 
	 * Devuelve una estructura List de objetos tipo Lector.
	 */
	public List<Lector> obtenerLectores(){
		Transaction tx = this.session.beginTransaction();
		List<Lector> lectores = null;
		try {
			Query query = session.createQuery("FROM Lector", Lector.class);
			lectores = query.getResultList(); 
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return lectores;
		
	}
}
