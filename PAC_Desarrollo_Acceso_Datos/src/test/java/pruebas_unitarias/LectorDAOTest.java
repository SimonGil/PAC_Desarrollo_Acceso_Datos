package pruebas_unitarias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data_access_object.LectorDAO;
import data_model.Lector;

public class LectorDAOTest {
    private SessionFactory sessionFactory;
    private Session session;
    private LectorDAO lectorDAO;

    @Before
    public void setUp() throws Exception {
        // Configurar la sesión de Hibernate
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();

        // Crear el objeto LectorDAO con la sesión de prueba
        lectorDAO = new LectorDAO(session);
    }

    @After
    public void tearDown() throws Exception {
        // Cerrar la sesión de Hibernate
        session.close();
        sessionFactory.close();
    }

    @Test
    /* Para comprobar que un lector ha sido insertado, debemos tratar de obtenerlo, y para
    obtenerlo, debemos insertarlo previamente, por lo que este método comprueba ambas funcionalidades.*/
    public void testInsertarObtenerLector() {
        // Creamos un nuevo Lector para la prueba
        Lector lector = new Lector();
        lector.setNombre("Pablo");
        lector.setApellidos("Garcia Perez");
        lector.setEmail("pgarciaperez@gmail.com");
        lector.setFechaNacimiento(LocalDate.of(1990, 12, 10));
        // Insertamos el Lector en la base de datos y guardamos su ID
        int idLector = lectorDAO.insertarLector(lector);

        // Verificar que el Lector se haya insertado correctamente
        assertNotNull(lectorDAO.obtenerLector(idLector));
    }
    
    @Test
    public void testBorrarLector() {
    	//Insertamos un lector en la base de datos y guardamos su ID
    	Lector lector = new Lector();
    	lector.setNombre("Lector");
    	int idLector = lectorDAO.insertarLector(lector);
        // Borramos el Lector de la base de datos
        lectorDAO.borrarLector(idLector);
        // Comprobamos que el Lector se haya borrado correctamente
        assertNull(lectorDAO.obtenerLector(idLector));
    }

    @Test
    public void testActualizarLector() {
        // Crear un nuevo Lector para la prueba
        Lector lector = new Lector();
        lector.setNombre("Pablo");
        lector.setApellidos("Garcia Perez");
        lector.setEmail("pgarciaperez@gmail.com");
        lector.setFechaNacimiento(LocalDate.of(1990, 12, 10));

        // Insertamos el Lector en la base de datos obteniendo su ID y asignandosela al objeto Lector
        int idLector = lectorDAO.insertarLector(lector);
        lector.setIdLector(idLector);
        // Modificamos el nombre del Lector
        lector.setNombre("Nuevo nombre");

        // Obtenemos y actualizamos el Lector en la base de datos
        lectorDAO.actualizarLector(lector);

        // Obtenemos el Lector actualizado de la base de datos
        Lector lectorActualizado = lectorDAO.obtenerLector(lector.getIdLector());

        // Verificar que el nombre del Lector se haya actualizado correctamente
        assertEquals("Nuevo nombre", lectorActualizado.getNombre());
    }

    @Test
    public void testObtenerLectores() {
        //Comprobamos el numero de lectores antes de la inserción
    	int numLectoresObtenidosAntes = lectorDAO.obtenerLectores().size();
    	
    	// Insertamos algunos lectores de prueba en la base de datos
        Lector lector1 = new Lector();
        lector1.setNombre("Lector 1");
        lectorDAO.insertarLector(lector1);

        Lector lector2 = new Lector();
        lector2.setNombre("Lector 2");
        lectorDAO.insertarLector(lector2);

        Lector lector3 = new Lector();
        lector3.setNombre("Lector 3");
        lectorDAO.insertarLector(lector3);

        // Obtener la lista de lectores de la base de datos
        List<Lector> lectores = lectorDAO.obtenerLectores();

        // Verificar que se hayan obtenido todos los lectores esperados
        assertEquals(numLectoresObtenidosAntes + 3, lectores.size());
    }
}