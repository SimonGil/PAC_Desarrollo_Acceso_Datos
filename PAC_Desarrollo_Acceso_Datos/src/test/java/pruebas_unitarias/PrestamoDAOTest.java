package pruebas_unitarias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data_access_object.LectorDAO;
import data_access_object.LibroDAO;
import data_access_object.PrestamoDAO;
import data_model.Lector;
import data_model.Libro;
import data_model.Prestamo;

public class PrestamoDAOTest {
    private SessionFactory sessionFactory;
    private Session session;
    private PrestamoDAO prestamoDAO;
    private LectorDAO lectorDAO;
    private LibroDAO libroDAO;
    
    @Before
    public void setUp() throws Exception {
        // Configurar la sesión de Hibernate
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();

        // Crear los objetos DAO con la sesión de prueba. Necesitaremos
        //LibroDAO y LectorDAO para probar varios métodos de la clase PrestamoDAO
        prestamoDAO = new PrestamoDAO(session);
        lectorDAO = new LectorDAO(session);
        libroDAO = new LibroDAO(session);
    }

    @After
    public void tearDown() throws Exception {
        // Cerrar la sesión de Hibernate
        session.close();
        sessionFactory.close();
    }
    
    @Test
    /* Para comprobar que un prestamo ha sido insertado, debemos tratar de obtenerlo, y para
       obtenerlo, debemos insertarlo previamente, por lo que este método comprueba ambas funcionalidades.*/
    public void testInsertarYObtenerPrestamo() {
        //Para insertar un préstamo necesitamos un lector y un libro, asi que los instanciamos e insertamos
    	//en la base de datos.
    	Lector lector = new Lector();
    	lector.setNombre("Lector 1");
    	lectorDAO.insertarLector(lector);
    	Libro libro = new Libro();
    	libro.setTitulo("Libro 1");
    	libroDAO.insertarLibro(libro);
    	//Llamamos al metodo insertarPrestamo y obtenemos el id del prestamo insertado.
    	int idPrestamo = prestamoDAO.insertarPrestamo(libro, lector);
    	//Verificamos que se haya insertado y que se pueda obtener
    	assertNotNull(prestamoDAO.obtenerPrestamo(idPrestamo));
    }
    @Test
    public void testBorrarPrestamo() {
    	//Insertamos un lector y un libro para poder insertar el prestamo.
    	Lector lector = new Lector();
    	lector.setNombre("Lector 1");
    	lectorDAO.insertarLector(lector);
    	Libro libro = new Libro();
    	libro.setTitulo("Libro 1");
    	libroDAO.insertarLibro(libro);
    	//Insertamos el prestamo y guardamos su ID
    	int idPrestamo  = prestamoDAO.insertarPrestamo(libro, lector);
    	//Eliminamos el préstamo 
    	prestamoDAO.borrarPrestamo(idPrestamo);
    	//Comprobamos que el préstamo ha sido eliminado ya que no podemos obtenerlo
    	assertNull(prestamoDAO.obtenerPrestamo(idPrestamo));
    }
    @Test
    public void testActualizarPrestamo() {
        // Insertamos un nuevo prestamo con un Libro y un Lector
    	Lector lector = new Lector();
    	lector.setNombre("Lector 1");
    	lectorDAO.insertarLector(lector);
    	Libro libro = new Libro();
    	libro.setTitulo("Libro 1");
    	libroDAO.insertarLibro(libro);
    	//Llamamos al metodo insertarPrestamo y obtenemos el id del prestamo insertado.
    	int idPrestamo = prestamoDAO.insertarPrestamo(libro, lector);
    	//Obtenemos el prestamo de la base de datos y lo modificamos asignándole fecha de devolución
    	Prestamo prestamo = prestamoDAO.obtenerPrestamo(idPrestamo);
    	prestamo.setFechaDevolucion(LocalDate.of(1990, 2, 2));
    	//Llamamos al método actualizarPrestamo para realizar la actualización
    	prestamoDAO.actualizarPrestamo(prestamo);
    	//Volvemos a obtener el prestamo de la base de datos para comprobar si su fecha de devolución ha 
    	//sido actualizada.
        assertEquals(prestamoDAO.obtenerPrestamo(idPrestamo).getFechaDevolucion(),
        		LocalDate.of(1990, 2, 2));
    }
    
    @Test
    public void testObtenerHistorialPrestamos() {
    	//Instanciamos un lector y dos libros, y los guardamos en la base de datos:
    	Lector lector = new Lector();
    	lector.setNombre("Lector 1");
    	int idLector = lectorDAO.insertarLector(lector);
    	Libro libro1 = new Libro();
    	libro1.setTitulo("Libro 1");
    	int idLibro1 = libroDAO.insertarLibro(libro1);
    	Libro libro2 = new Libro();
    	libro1.setTitulo("Libro 2");
    	int idLibro2 = libroDAO.insertarLibro(libro2);
    	//Prestamos ambos libros al lector
    	prestamoDAO.insertarPrestamo(libro1,lector);
    	prestamoDAO.insertarPrestamo(libro2,lector);
    	//Obtenemos el historial de prestamos del lector y comprobamos
    	//que los libros de los prestamos son los libros que hemos prestado
    	List<Prestamo> prestamos = prestamoDAO.obtenerHistorialPrestamos(idLector);
    	for(Prestamo p : prestamos) {
    		assertTrue(p.getLibro() == libro1 || p.getLibro() == libro2);
    	}		
    }
    
    @Test 
    public void testObtenerPrestamoPorLibro() {
    	//Instaciamos un Libro y un Lector, y los insertamos en la base de datos
    	Libro libro = new Libro();
    	libro.setTitulo("Libro");
    	int idLibro = libroDAO.insertarLibro(libro);
    	Lector lector = new Lector();
    	lector.setNombre("Lector");
    	lectorDAO.insertarLector(lector);
    	//Insertamos el prestamo en la base de datos y guardamos su id
    	int idPrestamo = prestamoDAO.insertarPrestamo(libro, lector);
    	//Obtenemos el prestamo utilizando el metodo obtenerPrestamoPorLibro
    	Prestamo prestamo = prestamoDAO.obtenerPrestamoPorLibro(idLibro);
    	//Comprobamos que el prestamo obtenido con el metodo anterior tenga el mismo id
    	//que el prestamo insertado en la base de datos.
    	assertEquals(idPrestamo, prestamo.getIdPrestamo());
    	
    }
}
