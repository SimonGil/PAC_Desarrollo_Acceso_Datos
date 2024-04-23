package data_access_object;

import org.hibernate.Session;


/* Clase abstracta Data Access Object. 
* 
* Actúa como clase DAO genérica de la que heredarán
*  todas las clases Data Access Object de la aplicación. 
* @author Simon Gil
*/
public abstract class EntidadDAO {
	protected Session session;
	//Constructor que recibe un objeto Session por parámetro
	public EntidadDAO(Session session) {
		this.session = session;
	}

}
