package pruebas_unitarias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

public class LibroDAOTest {
    private SessionFactory sessionFactory;
    private Session session;
    private LibroDAO libroDAO;

    @Before
    public void setUp() throws Exception {
        // Configurar la sesión de Hibernate
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();

        // Crear el objeto LibroDAO con la sesión de prueba
        libroDAO = new LibroDAO(session);
    }

    @After
    public void tearDown() throws Exception {
        // Cerrar la sesión de Hibernate
        session.close();
        sessionFactory.close();
    }

    @Test
    public void testBorrarLibro() {
    	Libro libro = new Libro();
    	libro.setTitulo("Libro de prueba");
    	int idLibro = libroDAO.insertarLibro(libro);
    	libroDAO.borrarLibro(idLibro);
    	assertNull(libroDAO.obtenerLibro(idLibro));
    }
    @Test
    //Para comprobar si un libro se ha insertado, necesitamos obtenerlo, y para obtenerlo
    //necesitamos insertarlo previamente, por lo que ambos métodos se prueban en este test
    public void testInsertarYObtenerLibro() {
    	//Creamos un nuevo libro y lo insertamos guardando su ID
    	Libro libro = new Libro();
    	libro.setTitulo("Titulo");
    	int idLibro = libroDAO.insertarLibro(libro);
    	/*Comprobamos si el libro se ha insertado y lo podemos obtener, 
    	evaluando si el titulo del libro obtenido es igual al del libro insertado*/
    	assertTrue(libroDAO.obtenerLibro(idLibro).getTitulo().equals("Titulo"));
    }
    @Test
    public void testActualizarLibro() {
        // Crear un nuevo Libro para la prueba
        Libro libro = new Libro();
        libro.setTitulo("Título de prueba");
        libro.setAutor("Autor de prueba");

        // Insertar el Libro en la base de datos
        libroDAO.insertarLibro(libro);

        // Modificar el título del Libro
        libro.setTitulo("Nuevo título");

        // Actualizar el Libro en la base de datos
        libroDAO.actualizarLibro(libro);

        // Obtener el Libro actualizado de la base de datos
        Libro libroActualizado = libroDAO.obtenerLibro(libro.getIdLibro());

        // Verificar que el título del Libro se haya actualizado correctamente
        assertEquals("Nuevo título", libroActualizado.getTitulo());
    }

    @Test
    public void testObtenerLibros() {
        //Obtenemos el numero actual de libros
    	int numLibrosAntes = libroDAO.obtenerLibros().size();
    	// Insertamos tres libros de prueba en la base de datos
        Libro libro1 = new Libro();
        libro1.setTitulo("Libro 1");
        libro1.setAutor("Autor 1");
        libroDAO.insertarLibro(libro1);

        Libro libro2 = new Libro();
        libro2.setTitulo("Libro 2");
        libro2.setAutor("Autor 2");
        libroDAO.insertarLibro(libro2);

        Libro libro3 = new Libro();
        libro3.setTitulo("Libro 3");
        libro3.setAutor("Autor 3");
        libroDAO.insertarLibro(libro3);

        // Obtenemos la lista de libros de la base de datos
        List<Libro> libros = libroDAO.obtenerLibros();
        // Verificamos que se hayan obtenido todos los libros esperados
        assertEquals(numLibrosAntes + 3, libros.size());
    }
    @Test
    public void testObtenerLibrosDisponibles() {
        //Obtenemos el numero actual de libros
    	int numLibrosAntes = libroDAO.obtenerLibros().size();
    	// Insertamos tres libros de prueba en la base de datos, dos disponibles y uno no.
        Libro libro1 = new Libro();
        libro1.setTitulo("Libro 1");
        libro1.setAutor("Autor 1");
        libro1.setDisponible(true);
        libroDAO.insertarLibro(libro1);

        Libro libro2 = new Libro();
        libro2.setTitulo("Libro 2");
        libro2.setAutor("Autor 2");
        libro2.setDisponible(true);
        libroDAO.insertarLibro(libro2);

        Libro libro3 = new Libro();
        libro3.setTitulo("Libro 3");
        libro3.setAutor("Autor 3");
        libro3.setDisponible(false);
        libroDAO.insertarLibro(libro3);

        // Obtenemos la lista de libros disponibles de la base de datos
        List<Libro> libros = libroDAO.obtenerLibrosDisponibles();

        //Filtramos la lista, eliminando los libros que no están disponibles
        //su tamaño debería quedar a 0.
        List<Libro> librosNoDisponibles = libros.stream()
        		.filter(l-> !l.isDisponible()).collect(Collectors.toList());
        // Verificamos que la lista anterior está vacía, y que la lista de libros disponibles sea igual 
        //o mayor que 2
        assertEquals(0, librosNoDisponibles.size());
        assertTrue(libros.size() >= 2);
    }
    @Test
    public void testLibrosActualmentePrestadosLector() {
    	// Insertamos tres libros de prueba en la base de datos y guardamos sus ID
        Libro libro1 = new Libro();
        libro1.setTitulo("Libro 1");
        libro1.setAutor("Autor 1");
        int idLibro1 = libroDAO.insertarLibro(libro1);

        Libro libro2 = new Libro();
        libro2.setTitulo("Libro 2");
        libro2.setAutor("Autor 2");
        int idLibro2 = libroDAO.insertarLibro(libro2);

        Libro libro3 = new Libro();
        libro3.setTitulo("Libro 3");
        libro3.setAutor("Autor 3");
        int idLibro3 = libroDAO.insertarLibro(libro3);
        //Insertamos un lector y guardamos su ID
        Lector lector1 = new Lector();
        lector1.setNombre("Pablo");
        lector1.setApellidos("Garcia Villares");
        lector1.setEmail("pgarciavilla@gmail.com");
        lector1.setFechaNacimiento(LocalDate.of(1990,2,2));
        LectorDAO lectorDAO = new LectorDAO(session);
        int idLector = lectorDAO.insertarLector(lector1);
        //Insertamos 3 préstamos al lector, uno de ellos lo actualizamos
        //con fecha de devolución, por lo que no debería aparecer como prestado.
        PrestamoDAO prestamoDAO = new PrestamoDAO(session);
        prestamoDAO.insertarPrestamo(libro1, lector1);
        prestamoDAO.insertarPrestamo(libro2, lector1);
        int idPrestamoDevuelto = prestamoDAO.insertarPrestamo(libro3, lector1);
        Prestamo prestamoDevuelto = prestamoDAO.obtenerPrestamo(idPrestamoDevuelto);
        prestamoDevuelto.setFechaDevolucion(LocalDate.of(2010, 4, 5));
        prestamoDAO.actualizarPrestamo(prestamoDevuelto);
        
        //Llamamos al método librosActualmentePrestados con el id del lector, su tamaño deberia ser 2
        assertTrue(libroDAO.librosActualmentePrestadosLector(idLector).size() == 2);

    }
}