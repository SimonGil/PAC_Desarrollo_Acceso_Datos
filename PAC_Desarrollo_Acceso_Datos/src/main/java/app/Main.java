package app;

import java.time.LocalDate;

import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import data_access_object.LectorDAO;
import data_access_object.LibroDAO;
import data_access_object.PrestamoDAO;
import data_model.Lector;
import data_model.Libro;

/**
 * Clase Main del proyecto
 * 
 * Contiene el método main que controla la ejecución de la aplicación y varios métodos 
 * para iniciar cada uno de los menús en función de la interacción del usuario.
 * También establece la conexión con la base de datos creando una Session que se irá pasando 
 * entre los diferentes menús.
 * 
 * @author Simon Gil
 */
public class Main {
	/*
	 * Método main de la aplicación
	 * 
	 * Inicializa la configuración de Hibernate, crea una Session
	 * y una instancia de BibliotecaService, para después inicar el
	 * menú principal de la aplicación.
	 */
	
	public static void main(String[] args) {
		Configuration cfg = new Configuration().configure();
		SessionFactory sessionFact = cfg.buildSessionFactory(new StandardServiceRegistryBuilder().configure().build());
		Session session = sessionFact.openSession();
		iniciarMenuPrincipal(session);
	}

	/*
	 * Método con la lógica de ejecución del menú principal
	 * 
	 */
	static public void iniciarMenuPrincipal(Session session) {
		Scanner scanner = new Scanner(System.in);
		BibliotecaService bs = new BibliotecaService(session);
		int opcion = 100;
		do {
			bs.printSeparador();
			System.out.println("\t\t\t\t\t\t BIBLIOTECA");
			bs.printSeparador();
			System.out.println("1-Gestionar Préstamos");
			System.out.println("2-Gestionar Libros");
			System.out.println("3-Gestionar Lectores");
			System.out.println("0-Salir del programa");
			bs.printSeparador();
			System.out.print("Seleccione una opción: ");
			opcion = bs.leerEntero(scanner);
			switch (opcion) {
			case 1:
				iniciarMenuPrestamos(session);
				break;
			case 2:
				iniciarMenuLibros(session);
				break;
			case 3:
				iniciarMenuLectores(session);
				break;
			case 0:
				System.out.println("Saliendo del programa...");
				break;
			default:
				System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
			}
		} while(opcion != 0);
		scanner.close();

	}

	/*
	 * Método con la lógica de ejecución del menú de gestión de préstamos.
	 * 
	 */
	public static void iniciarMenuPrestamos(Session session) {
		BibliotecaService bs = new BibliotecaService(session);
		int opcion;
		Scanner scanner = new Scanner(System.in);
		do {
			bs.printSeparador();
			System.out.println("\t\t\t\t\t\t GESTIÓN DE PRÉSTAMOS");
			bs.printSeparador();
			System.out.println("1-Nuevo préstamo");
			System.out.println("2-Devolución de préstamo");
			System.out.println("3-Borrar préstamo");
			System.out.println("4-Consultar historial de préstamos de un lector");
			System.out.println("5-Actualizar información de un préstamo");
			System.out.println("0-Volver al menú principal");
			bs.printSeparador();
			System.out.print("Seleccione una opción: ");
			opcion = bs.leerEntero(scanner);
			switch (opcion) {
			case 1:
				bs.prestarLibro();
				break;
			case 2:
				bs.devolucionPrestamo();
				break;
			case 3:
				int idPrestamoDelete;
				bs.mostrarPrestamos();
				PrestamoDAO presDAO = new PrestamoDAO(session);
				if (presDAO.obtenerPrestamos().size() > 0) {
					System.out.println("Introduzca el ID del préstamo de la lista anterior que desea eliminar: ");
					idPrestamoDelete = bs.leerEntero(scanner);
					if (presDAO.obtenerPrestamo(idPrestamoDelete) != null) {
						bs.eliminarPrestamo(idPrestamoDelete);
					} else
						System.out.println("Error: No se ha encontrado un préstamo con el ID especificado.");
				} else
					System.out.println("No es posible eliminar registros de una tabla vacía.");
				break;
			case 4:
				bs.mostrarLectores();
				LectorDAO lecdao = new LectorDAO(session);
				if (!lecdao.obtenerLectores().isEmpty()) {
					System.out.print(
							"Introduzca el ID de un lector de la lista sobre el que quiere realizar esta consulta: ");
					int idLector;
					idLector = bs.leerEntero(scanner);
					bs.mostrarHistorialPrestamos(idLector);
				} else {
					System.out.println("No se puede realizar la consulta porque no hay lectores en la base de datos.");
				}
				break;
			case 5:
				bs.modificarPrestamo();
				break;
			case 0:
				iniciarMenuPrincipal(session);
				break;
			default:
				System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
			}
		} while (opcion != 0);
		scanner.close();
	}

	/*
	 * Método con la lógica de ejecución del menú de gestión de préstamos.
	 * 
	 */
	static public void iniciarMenuLibros(Session session) {
		BibliotecaService bs = new BibliotecaService(session);
		int opcion;
		Scanner scanner = new Scanner(System.in);
		do {
			bs.printSeparador();
			System.out.println("\t\t\t\t\t\t GESTIÓN DE LIBROS");
			bs.printSeparador();
			System.out.println("1-Insertar libro");
			System.out.println("2-Actualizar información de un libro");
			System.out.println("3-Eliminar libro");
			System.out.println("4-Mostrar libros disponibles para préstamo");
			System.out.println("5-Mostrar todos los libros de la base de datos");
			System.out.println("0-Volver al menú principal");
			bs.printSeparador();
			System.out.print("Seleccione una opción: ");
			opcion = bs.leerEntero(scanner);
			switch (opcion) {
			case 1:
				String titulo, autor;
				int anyo;
				System.out.println("Introduzca el título del nuevo libro: ");
				titulo = scanner.nextLine();
				System.out.println("Introduzca su autor: ");
				autor = scanner.nextLine();
				System.out.println("Introduzca el año de publicación: ");
				anyo = bs.leerEntero(scanner);
				bs.insertarLibro(titulo, autor, anyo);
				break;
			case 2:
				bs.modificarLibro();
				break;
			case 3:
				int idLibro;
				bs.mostrarLibros();
				LibroDAO libdao = new LibroDAO(session);
				if (libdao.obtenerLibros().size() > 0) {
					System.out.print("Elija un libro de la lista e introduzca su ID para eliminarlo: ");
					idLibro = bs.leerEntero(scanner);
					Libro libro = libdao.obtenerLibro(idLibro);
					if (libro != null) {
						System.out.println("Seguro que desea eliminar el libro titulado: " + libro.getTitulo()
								+ "? (Y:Si, N:No):");
						String eliminarLibro = scanner.nextLine();
						if (eliminarLibro.toUpperCase().equals("Y")) {
							bs.eliminarLibro(idLibro);
						} else {
							System.out.println("El libro no será eliminado.");
							break;
						}
					} else
						System.out.println("Error: No se ha encontrado un libro con el ID especificado");
				} else
					System.out.println("No es posible eliminar registros de una tabla vacía.");
				break;
			case 4:
				bs.mostrarLibrosDisponibles();
				break;
			case 5:
				bs.mostrarLibros();
				break;
			case 0:
				iniciarMenuPrincipal(session);
				break;
			default:
				System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
			}
		} while (opcion != 0);
		scanner.close();
	}

	/*
	 * Método con la lógica de ejecución del menú de gestión de préstamos.
	 * 
	 */
	static public void iniciarMenuLectores(Session session) {
		BibliotecaService bs = new BibliotecaService(session);
		int opcion;
		Scanner scanner = new Scanner(System.in);
		do {
			bs.printSeparador();
			System.out.println("\t\t\t\t\t\t GESTIÓN DE LECTORES");
			bs.printSeparador();
			System.out.println("1-Nuevo lector");
			System.out.println("2-Actualizar información de un lector");
			System.out.println("3-Eliminar lector");
			System.out.println("4-Consultar libros prestados a un lector");
			System.out.println("0-Volver al menú principal");
			bs.printSeparador();
			System.out.print("Seleccione una opción: ");
			opcion = bs.leerEntero(scanner);
			switch (opcion) {
			case 1:
				String nombre, apellidos, email;
				LocalDate fechaDeNacimiento;
				System.out.print("Introduzca el nombre: ");
				nombre = scanner.nextLine();
				System.out.print("Introduzca los apellidos: ");
				apellidos = scanner.nextLine();
				System.out.print("Introduzca e-mail: ");
				email = bs.leerEmail(scanner);
				System.out.print("Ahora introduzca la fecha de nacimiento. ");
				fechaDeNacimiento = bs.preguntarFecha();
				bs.insertarLector(nombre, apellidos, email, fechaDeNacimiento);
				break;
			case 2:
				bs.modificarLector();
				break;
			case 3:
				int idLector;
				bs.mostrarLectores();
				LectorDAO lecdao = new LectorDAO(session);
				if (lecdao.obtenerLectores().size() > 0) {
					System.out.print("Introduzca el ID del lector de la lista anterior que desea eliminar: ");
					idLector = bs.leerEntero(scanner);
					Lector lector = lecdao.obtenerLector(idLector);
					if (lector != null) {
						System.out.println("Seguro que desea eliminar al lector: " + lector.getNombre() + " "
								+ lector.getApellidos()
								+ "? La acción eliminará también todos sus préstamos (Y:Si, N:No):");
						String input = scanner.nextLine();
						if (input.toUpperCase().equals("Y")) {
							bs.eliminarLector(idLector);

						} else {
							System.out.println("El lector no será eliminado.");
						}
					} else
						System.out.println("Error: No se ha encontrado a un lector con el ID especificado");
				} else
					System.out.println("No es posible eliminar registros de una tabla vacía.");
				break;
			case 4:
				bs.mostrarLectores();
				System.out.print("Introduzca el ID del lector sobre el que quiere realizar esta consulta: ");
				idLector = bs.leerEntero(scanner);
				bs.mostrarLibrosPrestadosLector(idLector);
				break;

			case 0:
				iniciarMenuPrincipal(session);
				break;
			default:
				System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
			}

		} while (opcion != 0);
		scanner.close();
	}


}
