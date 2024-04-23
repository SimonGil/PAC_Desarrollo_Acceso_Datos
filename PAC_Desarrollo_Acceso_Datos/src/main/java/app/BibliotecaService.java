package app;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;

import data_access_object.LectorDAO;
import data_access_object.LibroDAO;
import data_access_object.PrestamoDAO;
import data_model.Lector;
import data_model.Libro;
import data_model.Prestamo;

/**
 * Clase de servicio para la aplicación
 * 
 * Esta clase utiliza las clases Data Access Object de cada entidad e implementa
 * los métodos necesarios para el funcionamiento de la aplicación, bien combinando los métodos
 * de las clases DAO con la entrada de datos por parte del usuario, o bien adaptando
 * los mismos para que reciban parámetros "introducibles" por el usuario.
 * 
 * También incluye métodos de utilidad para la lectura y validación de datos.
 * 
 * @author Simon Gil
 */
public class BibliotecaService {
	private LibroDAO libroDAO;
	private LectorDAO lectorDAO;
	private PrestamoDAO prestamoDAO;

	public BibliotecaService(Session session) {
		/**
		 * Atributos de la clase. Esta clase tiene como atributos las instancias de las
		 * clases Data Access Object de todas las entidades de la aplicación.
		 */
		this.libroDAO = new LibroDAO(session);
		this.lectorDAO = new LectorDAO(session);
		this.prestamoDAO = new PrestamoDAO(session);
	}

	/*
	 * Método para mostrar una lista con todos los libros
	 * 
	 * Imprime por consola una lista con todos los libros de la base de datos.
	 */
	public void mostrarLibros() {
		printSeparador();
		List<Libro> libros = this.libroDAO.obtenerLibros();
		System.out.println("MOSTRANDO TODOS LOS LIBROS DE LA BASE DE DATOS");
		printSeparador();
		if (!libros.isEmpty()) {
			for (Libro l : libros) {
				System.out.println(l.toString());
			}
		} else {
			System.out.println("No se han encontrado libros en la base de datos.");
		}
		printSeparador();
	}

	/*
	 * Metodo para mostrar los libros disponibles
	 * 
	 * Imprime por consola los libros que puede ser prestados.
	 */
	public void mostrarLibrosDisponibles() {
		List<Libro> librosDisponibles = this.libroDAO.obtenerLibrosDisponibles();
		printSeparador();
		System.out.println("MOSTRANDO TODOS LOS LIBROS DISPONIBLES PARA PRÉSTAMO");
		printSeparador();
		if (!librosDisponibles.isEmpty()) {
			for (Libro l : librosDisponibles) {
				System.out.println(l.toString());
			}
		} else {
			System.out.println("No se ha encontrado ningún libro disponible en la base de datos.");
		}
		printSeparador();
	}

	/*
	 * Método para mostrar los libros prestados a un lector.
	 * 
	 * Recibe el ID de un lector y mostrará por consola una lista con los libros
	 * que están actualmente prestados al lector.
	 * También mostrará un mensaje oportuno si no hay ningún libro actualmente prestado al lector.
	 * 
	 */
	public void mostrarLibrosPrestadosLector(int idLector) {
		Lector lector = this.lectorDAO.obtenerLector(idLector);
		if (lector != null) {
			List<Libro> librosPrestadosLector = this.libroDAO.librosActualmentePrestadosLector(idLector);
			printSeparador();
			System.out.println("MOSTRANDO LIBROS PRESTADOS A " + lector.getNombre().toUpperCase() + " "
					+ lector.getApellidos().toUpperCase());
			printSeparador();
			if (!librosPrestadosLector.isEmpty()) {
				for (Libro l : librosPrestadosLector) {
					System.out.println(l.toString());
				}
			} else {
				System.out.println("No se ha encontrado ningún libro prestado al lector seleccionado.");
			}
			printSeparador();
		} else
			System.out.println("No se ha encontrado al lector con ID: " + idLector);
	}

	/*
	 * Método para mostrar el historial de préstamos de un lector
	 * 
	 * Recibe el ID de un lector y muestra por consola un historial con 
	 * todos los prestamos realizados por el lector, incluyendo los ya devueltos.
	 * Muestra mensajes oportunos en caso de que el historial esté vacío o no
	 * haya sido posible encontrar el historial.
	 * 
	 */
	public void mostrarHistorialPrestamos(int idLector) {
		try {
			Lector lector = this.lectorDAO.obtenerLector(idLector);
			printSeparador();
			System.out.println("MOSTRANDO HISTORIAL DE PRÉSTAMOS DE: " + lector.getNombre() + " " + lector.getApellidos());
			printSeparador();
			List<Prestamo> historial = this.prestamoDAO.obtenerHistorialPrestamos(idLector);
			if (!historial.isEmpty()) {
				for (Prestamo p : historial) {
					System.out.println(p.toString());
				}
			} else {
				System.out.println("El historial está vacío.");
				printSeparador();
			}
		} catch (Exception e) {
			System.out.println(
					"ERROR: No ha sido posible encontrar el historial. Asegúrese de haber introducido un ID de lector válido.");
			e.printStackTrace();
		}
	}

	/*
	 * Método para realizar un préstamo por consola
	 * 
	 * Ejecuta todo el diálogo por consola para obtener un objeto
	 * préstamo e insertarlo en la base de datos.
	 * 
	 */
	public void prestarLibro() {
		Scanner scanner = new Scanner(System.in);
		int idLibro, idLector;
		if (!libroDAO.obtenerLibros().isEmpty() && !lectorDAO.obtenerLectores().isEmpty()) {
			mostrarLibrosDisponibles();
			System.out.print("Busque el ID del libro a prestar en la lista anterior e introdúzcalo a continuación: ");
			idLibro = leerEntero(scanner);
			Libro libro = this.libroDAO.obtenerLibro(idLibro);
			if (libro != null) {
				mostrarLectores();
				System.out.print("Busque el ID del lector en la lista anterior e introdúzcalo a continuación: ");
				idLector = leerEntero(scanner);
				Lector lector = this.lectorDAO.obtenerLector(idLector);
				if (lector != null) {
					if (libro.isDisponible()) {
						this.prestamoDAO.insertarPrestamo(libro, lector);
						libro.setDisponible(false);
						this.libroDAO.actualizarLibro(libro);
						System.out.println("Préstamo realizado. Se ha prestado el libro con titulo: "
								+ libro.getTitulo() + ", a: " + lector.getNombre() + " " + lector.getApellidos());
					} else
						System.out.println("No se puede realizar el préstamo: El libro no está disponible");

				} else
					System.out.println("El ID del lector introducido no es válido, por lo que no se puede realizar el préstamo.");
			} else
				System.out.println("El ID del libro introducido no es válido, por lo que no se puede realizar el préstamo.");
		} else
			System.out.println("No se puede realizar un préstamo porque, "
					+ "o bien no hay libros disponibles, o no hay lectores en la base de datos.");
	}

	/*
	 * Método para devolver un préstamo por consola
	 * 
	 * Ejecuta un diálogo con el usuario para obtener los datos
	 * necesarios para la devolución de un préstamo, y actualiza
	 * la fecha de devolución del mismo.
	 * También actualiza la disponibilidad de un libro, que pasa a ser True 
	 * cuando se devuelve.
	 * 
	 */
	public void devolucionPrestamo() {
		int idLibro, idLector;
		LocalDate fecha;
		Scanner scanner = new Scanner(System.in);
		// Comprobamos si hay algún préstamo pendiente de devolución
		if (!prestamoDAO.obtenerPrestamos().stream().filter(p -> p.getFechaDevolucion() == null).toList().isEmpty()) {
			mostrarLectores();
			System.out.print("Introduzca el ID del lector de la lista que devolverá un préstamo: ");
			idLector = leerEntero(scanner);
			if (this.lectorDAO.obtenerLector(idLector) != null) {
				mostrarLibrosPrestadosLector(idLector);
				// Comprobamos si el lector tiene algún prestamo pendiente
				if (!this.libroDAO.librosActualmentePrestadosLector(idLector).isEmpty()) {
					System.out.print("Introduzca el ID del Libro de la lista anterior que será devuelto: ");
					idLibro = leerEntero(scanner);
					Libro libro = this.libroDAO.obtenerLibro(idLibro);
					if (libro != null && this.libroDAO.librosActualmentePrestadosLector(idLector).contains(libro)) {
						System.out.print("Desea utilizar la fecha actual(1) o introducirla manualmente(2): ");
						int opcionFecha = leerEntero(scanner);
						if (opcionFecha == 2) {
							fecha = preguntarFecha();
						} else
							fecha = LocalDate.now();

						try {
							Prestamo prestamo = this.prestamoDAO.obtenerPrestamoPorLibro(idLibro);
							// Si no se ha introducido fecha, tomamos la fecha actual
							if (fecha == null) {
								prestamo.setFechaDevolucion(LocalDate.now());
							} else
								prestamo.setFechaDevolucion(fecha);
							this.prestamoDAO.actualizarPrestamo(prestamo);
							// Actualizamos el libro devuelto para que esté disponible
							libro = prestamo.getLibro();
							libro.setDisponible(true);
							this.libroDAO.actualizarLibro(libro);
							System.out.println("El préstamo ha sido devuelto correctamente.");
						} catch (Exception e) {
							System.out.println("Error en la devolución del préstamo: ");
							e.printStackTrace();
						}
					} else
						System.out.println("El ID introducido no corresponde a ningún libro prestado a este lector.");
				} else
					System.out.println("El lector no tiene préstamos pendientes de devolución, seleccione otro lector o realice otra operación.");
			} else
				System.out.println("No ha sido posible obtener un lector con el ID introducido.");
		} else
			System.out.println("No hay ningún préstamo pendiente de devolución.");
	}

	/*
	 * Método para eliminar un préstamo de la base de datos
	 * 
	 * Recibe el ID de un préstamo y lo elimina de la base de datos.
	 * También actualiza el campo "disponible" del libro asignándole el valor
	 * "true" en caso de que el préstamo eliminado todavía no haya sido devuelto.
	 * (en este caso se entiende que el libro no había sido prestado y se ha insertado
	 * el préstamo por error).
	 */
	public void eliminarPrestamo(int idPrestamo) {
		try {
			Prestamo prestamo = this.prestamoDAO.obtenerPrestamo(idPrestamo);
			// Actualizamos el campo "disponible" del libro, si el préstamo no ha sido devuelto.
			if (prestamo.getFechaDevolucion() == null) {
				Libro libro = this.prestamoDAO.obtenerPrestamo(idPrestamo).getLibro();
				// Comprobamos que el libro obtenido no sea null, ya que podría haber sido
				// eliminado.
				if (libro != null) {
					libro.setDisponible(true);
					this.libroDAO.actualizarLibro(libro);
				}
			}
			this.prestamoDAO.borrarPrestamo(idPrestamo);
		} catch (Exception e) {
			System.out.println("No ha sido posible eliminar el préstamo. Compruebe que ha introducido un ID válido.");
		}
	}

	/*
	 * Método para insertar un libro
	 * 
	 * Recibe como parámetros dos Strings con título y autor del libro
	 * y un Integer con el año de publicación del libro.
	 * La disponibilidad sera true automáticamente.
	 * 
	 */
	public void insertarLibro(String titulo, String autor, int anoPublicacion) {
		Libro libro = new Libro();
		libro.setTitulo(titulo);
		libro.setAutor(autor);
		libro.setAnoPublicacion(anoPublicacion);
		// El libro siempre estará disponible en el momento de su inserción
		libro.setDisponible(true);
		try {
			this.libroDAO.insertarLibro(libro);
			System.out.println("El libro se ha insertado correctamente.");
		} catch (Exception e) {
			System.out.println("Se ha producido un error en la inserción del libro:");
			e.printStackTrace();
		}
	}

	/*
	 * Método para eliminar un libro
	 * 
	 * Recibe el ID de un libro y llama al método borrarLibro de la 
	 * clase LibroDAO. Imprime por consola el resultado de la eliminación. 
	 * Elimina también todos los préstamos que hacen referencia al libro
	 */
	public void eliminarLibro(int idLibro) {
		try {
			this.libroDAO.borrarLibro(idLibro);
		} catch (Exception e) {
			System.out.println("No ha sido posible eliminar el libro. Compruebe que ha introducido un ID válido.");
		}
	}

	/*
	 * Método para insertar un lector
	 * 
	 * Recibe como parámetros los datos necesarios para instanciar un
	 * objeto Lector, y después los inserta en la base de datos 
	 * llamando el método insertarLector de la clase LectorDAO.
	 */
	public void insertarLector(String nombre, String apellidos, String email, LocalDate fechaNacimiento) {
		Lector lector = new Lector();
		lector.setNombre(nombre);
		lector.setApellidos(apellidos);
		lector.setEmail(email);
		lector.setFechaNacimiento(fechaNacimiento);
		try {
			lectorDAO.insertarLector(lector);
			System.out.println("Lector insertado correctamente.");
		} catch (Exception e) {
			System.out.println("Error al insertar el nuevo lector:");
			e.printStackTrace();
		}
	}

	/*
	 * Método para eliminar a un lector
	 * 
	 * Recibe como parámetro el ID del lector a elminar.
	 * También elimina los préstamos de este lector en la tabla préstamo,
	 * para evitar que esta tenga referencias vacías.
	 */
	public void eliminarLector(int idLector) {
		try {
			this.lectorDAO.borrarLector(idLector);
			List<Prestamo> prestamos = prestamoDAO.obtenerHistorialPrestamos(idLector);
			for (Prestamo p : prestamos) {
				eliminarPrestamo(p.getIdPrestamo());
			}
		} catch (Exception e) {
			System.out.println("No ha sido posible eliminar el préstamo. Compruebe que ha introducido un ID válido.");
		}
	}
	
	/*
	 * Método para mostrar todos los préstamos por consola
	 * 
	 * Obtiene todos los préstamos de la base de datos y los muestra por consola.
	 * En caso de que esta lista esté vacía informa al usuario.
	 * 
	 */
	public void mostrarPrestamos() {
		List<Prestamo> prestamos = this.prestamoDAO.obtenerPrestamos();
		printSeparador();
		System.out.println("MOSTRANDO TODOS LOS PRÉSTAMOS DE LA BASE DE DATOS");
		printSeparador();
		if (!prestamos.isEmpty()) {
			for (Prestamo p : prestamos) {
				System.out.println(p.toString());
			}
		} else
			System.out.println("Actualmente no hay préstamos almacenados en la base de datos.");
		printSeparador();
	}

	/*
	 * Método para mostrar todos los lectores por consola
	 * 
	 * Obtiene todos los lectores de la base de datos y los muestra por consola.
	 * En caso de que esta tabla esté vacía informa al usuario.
	 * 
	 */
	public void mostrarLectores() {
		List<Lector> lectores = lectorDAO.obtenerLectores();
		printSeparador();
		System.out.println("MOSTRANDO TODOS LOS LECTORES DE LA BASE DE DATOS");
		printSeparador();
		if (!lectores.isEmpty()) {
			for (Lector l : lectores) {
				System.out.println(l.toString());
			}
		} else
			System.out.println("Actualmente no hay lectores almacenados en la base de datos.");
		printSeparador();
	}

	/*
	 * Método para modificar un libro por consola
	 * 
	 * Contiene toda la lógica del diálogo con el usuario para
	 * obtener los datos a actualizar y posteriormente realiza 
	 * la actualización del libro indicado.
	 * 
	 */
	public void modificarLibro() {
		Scanner scanner = new Scanner(System.in);
		int inputInt;
		String input;
		this.mostrarLibros();
		if (!libroDAO.obtenerLibros().isEmpty()) {
			System.out.println("Introduzca el ID del libro de la lista que desea modificar: ");
			inputInt = leerEntero(scanner);
			Libro libro = this.libroDAO.obtenerLibro(inputInt);
			if (libro != null) {
				// Modificar titulo
				System.out.println("Quieres modificar el Título del libro? (Y:Sí),(N:No): ");
				input = scanner.nextLine();
				if (input.toUpperCase().equals("Y")) {
					System.out.println("Introduzca el nuevo título a continuación: ");
					libro.setTitulo(scanner.nextLine());
				} else if (input.toUpperCase().equals("N")) {
					System.out.println("El título no será modificado.");
				} else
					System.out.println("Valor introducido incorrecto, el título no será modificado.");
				// Modificar autor
				System.out.println("Quieres modificar el Autor del libro? (Y:Sí),(N:No): ");
				input = scanner.nextLine();
				if (input.toUpperCase().equals("Y")) {
					System.out.println("Introduzca el nuevo Autor a continuación: ");
					libro.setAutor(scanner.nextLine());
				} else if (input.toUpperCase().equals("N")) {
					System.out.println("El autor no será modificado.");
				} else
					System.out.println("Valor introducido incorrecto, el autor no será modificado.");
				// Modificar año de publicación
				System.out.println("Quieres modificar el Año de publicación del libro? (Y:Sí),(N:No): ");
				input = scanner.nextLine();
				if (input.toUpperCase().equals("Y")) {
					System.out.println("Introduzca el nuevo año a continuación: ");
					inputInt = leerEntero(scanner);
					libro.setAnoPublicacion(inputInt);
				} else if (input.toUpperCase().equals("N")) {
					System.out.println("El año no será modificado.");
				} else
					System.out.println("Valor introducido incorrecto, el año no será modificado.");
				// Modificar disponibilidad
				System.out.println("Quieres modificar la disponibilidad del libro? (Y:Sí),(N:No): ");
				System.out.println("ATENCIÓN: Modifique este valor solo si tiene plena seguridad de la acción."
						+ " De lo contrario puede llevar a errores en la gestión de préstamos.");
				input = scanner.nextLine();
				if (input.toUpperCase().equals("Y")) {
					System.out.println("Introduzca el valor que quiere asignar (D:Disponible)(ND:No disponible) : ");
					input = scanner.nextLine();
					if (input.toUpperCase().equals("N")) {
						libro.setDisponible(true);
					} else if (input.toUpperCase().equals("ND")) {
						libro.setDisponible(false);
					} else
						System.out
								.println("No se ha introducido un valor válido, la disponibilidad no será modificada.");
				} else if (input.toUpperCase().equals("N")) {
					System.out.println("La disponibilidad no será modificada.");
				} else
					System.out.println("No se ha introducido un valor válido, la disponibilidad no será modificada.");
				// Realizar actualizacion
				this.libroDAO.actualizarLibro(libro);
				System.out.println("Libro actualizado con éxito, nuevos valores del libro:\n" + libro.toString());
			} else {
				System.out.println("No se ha podido obtener un libro con el ID especificado, vuelva a intentarlo.");
				this.modificarLibro();
			}
		} else {
			System.out.println("No es posible realizar la actualización de una tabla vacía.");
		}
	}
	
	/*
	 * Método para modificar un lector por consola
	 * 
	 * Contiene toda la lógica del diálogo con el usuario para
	 * obtener los datos a actualizar y posteriormente realiza 
	 * la actualización del lector indicado.
	 * 
	 */
	public void modificarLector() {
		Scanner scanner = new Scanner(System.in);
		int opcion;
		String input;
		this.mostrarLectores();
		if (!lectorDAO.obtenerLectores().isEmpty()) {
			System.out.println("Introduzca el ID del lector de la lista que desea modificar: ");
			opcion = leerEntero(scanner);
			Lector lector = this.lectorDAO.obtenerLector(opcion);
			if (lector != null) {
				// Modificar Nombre del lector
				System.out.println("Quieres modificar el Nombre del lector? (Y:Sí),(N:No): ");
				input = scanner.nextLine();
				if (input.toUpperCase().equals("Y")) {
					System.out.println("Introduzca el nuevo nombre a continuación: ");
					lector.setNombre(scanner.nextLine());
				} else if (input.toUpperCase().equals("N")) {
					System.out.println("El nombre no será modificado.");
				} else
					System.out.println("Valor introducido incorrecto, el nombre no será modificado.");
				// Modificar Apellidos del lector
				System.out.println("Quieres modificar los Apellidos del lector? (Y:Sí),(N:No): ");
				input = scanner.nextLine();
				if (input.toUpperCase().equals("Y")) {
					System.out.println("Introduzca los nuevos apellidos a continuación: ");
					lector.setApellidos(scanner.nextLine());
				} else if (input.toUpperCase().equals("N")) {
					System.out.println("Los apellidos no serán modificados.");
				} else
					System.out.println("Valor introducido incorrecto, los apellidos no serán modificados.");
				// Modificar e-mail del lector
				System.out.println("Quieres modificar el e-mail del lector? (Y:Sí),(N:No): ");
				input = scanner.nextLine();
				if (input.toUpperCase().equals("Y")) {
					System.out.println("Introduzca el nuevo e-mail a continuación: ");
					lector.setEmail(leerEmail(scanner));
				} else if (input.toUpperCase().equals("N")) {
					System.out.println("El e-mail no será modificado.");
				} else
					System.out.println("Valor introducido incorrecto, el e-mail no será modificado.");
				// Modificar fecha de nacimiento del lector
				System.out.println("Quieres modificar la fecha de nacimiento del lector? (Y:Sí),(N:No): ");
				input = scanner.nextLine();
				if (input.toUpperCase().equals("Y")) {
					LocalDate fecha = this.preguntarFecha();
					lector.setFechaNacimiento(fecha);
				} else if (input.toUpperCase().equals("N")) {
					System.out.println("La fecha de nacimiento no será modificada.");
				} else
					System.out.println("Valor introducido incorrecto, la fecha de nacimiento no será modificada.");
				// Realizar actualizacion
				this.lectorDAO.actualizarLector(lector);
				System.out.println("Lector actualizado con éxito, nuevos valores del lector:\n" + lector.toString());
			} else {
				System.out.println("No se ha podido obtener un lector con el ID especificado, vuelva a intentarlo.");
				this.modificarLector();
			}
		} else {
			System.out.println("No es posible realizar la actualización de una tabla vacía.");
		}
	}
		/*
		 * Método para modificar un préstamo por consola
		 * 
		 * Contiene toda la lógica del diálogo con el usuario para
		 * obtener los datos a actualizar y posteriormente realiza 
		 * la actualización del préstamo indicado.
		 * 
		 */
		public void modificarPrestamo() {
			Scanner scanner = new Scanner(System.in);
			int inputInt;
			String input;
			this.mostrarPrestamos();
			// Comprobamos si hay préstamos en la base de datos, de lo contrario no se podrá
			// actualizar.
			if (!prestamoDAO.obtenerPrestamos().isEmpty()) {
				System.out.println("Introduzca el ID del prestamo de la lista anterior que desea modificar: ");
				inputInt = leerEntero(scanner);
				Prestamo prestamo = this.prestamoDAO.obtenerPrestamo(inputInt);
				// Comprobamos si se ha podido obtener el préstamo con el ID introducido
				if (prestamo != null) {
					// Modificar ID Libro prestado
					System.out.println("Quieres modificar el ID del libro prestado? (Y:Sí),(N:No): ");
					input = scanner.nextLine();
					if (input.toUpperCase().equals("Y")) {
						System.out.println("Introduzca el nuevo ID a continuación: ");
						inputInt = leerEntero(scanner);
						Libro libro = this.libroDAO.obtenerLibro(inputInt);
						// Si podemos obtener el libro, modificamos el ID en el prestamo.
						if (libro != null) {
							prestamo.setLibro(libro);
						} else
							System.out.println(
									"No existe un libro con el ID introducido, por lo que el ID actual no será modificado.");
					} else if (input.toUpperCase().equals("N")) {
						System.out.println("El ID del libro prestado no será modificado.");
					} else
						System.out
								.println("Valor introducido incorrecto, el ID del libro prestado no será modificado.");
					// Modificar ID Lector
					System.out.println("Quieres modificar el ID del Lector? (Y:Sí),(N:No): ");
					input = scanner.nextLine();
					if (input.toUpperCase().equals("Y")) {
						System.out.println("Introduzca el ID del nuevo Lector a continuación: ");
						inputInt = leerEntero(scanner);
						Lector lector = this.lectorDAO.obtenerLector(inputInt);
						if (lector != null) {
							prestamo.setLector(lector);
						} else
							System.out.println("No existe un lector con el ID introducido, por lo que el ID actual no será modificado.");
					} else if (input.toUpperCase().equals("N")) {
						System.out.println("El ID del Lector no será modificado.");
					} else
						System.out.println("Valor introducido incorrecto, el ID del Lector no será modificado.");
					// Modificar fecha de préstamo
					System.out.println("Quieres modificar la fecha de préstamo? (Y:Sí),(N:No): ");
					input = scanner.nextLine();
					if (input.toUpperCase().equals("Y")) {
						System.out.println("Introduzca la nueva fecha a continuación: ");
						LocalDate fecha = preguntarFecha();
						prestamo.setFechaPrestamo(fecha);
					} else if (input.toUpperCase().equals("N")) {
						System.out.println("La fecha de préstamo no será modificada.");
					} else
						System.out.println("Valor introducido incorrecto, la fecha de préstamo no será modificada.");
					// Modificar fecha de devolución
					System.out.println("Quieres modificar la fecha de devolución del préstamo? (Y:Sí),(N:No): ");
					input = scanner.nextLine();
					if (input.toUpperCase().equals("Y")) {
						System.out.println("Introduzca la nueva fecha a continuación: ");
						LocalDate fecha = preguntarFecha();
						prestamo.setFechaDevolucion(fecha);
					} else if (input.toUpperCase().equals("N")) {
						System.out.println("La fecha de devolución no será modificada.");
					} else
						System.out.println("Valor introducido incorrecto, la fecha de devolución no será modificada.");
					// Realizar actualizacion
					this.prestamoDAO.actualizarPrestamo(prestamo);
					System.out.println(
							"Préstmo actualizado con éxito, nuevos valores del préstamo:\n" + prestamo.toString());
				} else {
					System.out.println(
							"No se ha podido obtener un préstamo con el ID especificado, vuelva a intentarlo.");
					this.modificarPrestamo();
				}
			} else {
				System.out.println("No es posible realizar la actualización de una tabla vacía.");
			}
		}

	/*
	 * Método para obtener un LocalDate con la fecha introducida por el usuario.
	 * 
	 * Gestiona las entradas con formato incorrecto y se repite recursivamente hasta
	 * que se haya introducido una fecha válida.
	 * 
	 */
	public LocalDate preguntarFecha() {
		Scanner scanner = new Scanner(System.in);
		String strFecha;
		System.out.print("La fecha debe ser introducida con el siguiente formato (DD/MM/AAAA):");
		strFecha = scanner.nextLine();
		try {
			String[] fechaSplit = strFecha.split("/");
			LocalDate fecha = LocalDate.of(Integer.parseInt(fechaSplit[2]), Integer.parseInt(fechaSplit[1]),
					Integer.parseInt(fechaSplit[0]));
			return fecha;	
		} catch (Exception e) {
			System.out.println("Error: El formato de la fecha introducida no es válido.");
			return preguntarFecha();
		}
	}
	
	/*
	 * Método para leer números enteros por teclado
	 * 
	 * Recibe un objeto de tipo scanner y realiza la lectura de un número entero
	 * por teclado. Se repite hasta que un número válido es introducido por el usuario.
	 * También consume el salto de línea tras la llamada al método nextLine para evitar 
	 * errores de lectura.
	 * 
	 */
	public int leerEntero(Scanner scanner) {
		int opcion = 0;
		boolean numValido = false;
		try {
			do {
				try {
					opcion = scanner.nextInt();
					numValido = true;
				} catch (InputMismatchException e) {
					scanner.nextLine(); // Consumimos el salto de línea para evitar que el bucle se repita infinitamente.
					System.out.println("Entrada no válida. Debe ingresar un número entero:");
				}
			} while (!numValido);
			scanner.nextLine(); // Consumimos el salto de linea despues de la lectura
		} catch (NoSuchElementException e) {
			//Capturamos esta excepción ya que se produce en la salida del programa 
			//al intentar leer la entrada. 
		}
		return opcion;
	}
	/*
	 * Método para leer y validar e-mail
	 * 
	 * Recibe un objeto de tipo scanner y realiza la lectura de un String.
	 * Comprueba si el String corresponde a un email válido o de lo contrario
	 * solicita al usuario reintentar la inserción.
	 */
	public String leerEmail(Scanner scanner) {
		String email;
		boolean emailValido = false;
		String regexEmailValido = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern patternEmailValido = Pattern.compile(regexEmailValido);
		do {
			email = scanner.nextLine();
			Matcher matcher = patternEmailValido.matcher(email);
			if(matcher.matches()) {
				emailValido = true;
			}else {
				System.out.println("El e-mail introducido no es correcto, por favor, vuelva a intentarlo:");
			}
		}while(!emailValido);
		return email;
	}

	/*
	 * Método para imprimir un separador en consola
	 * 
	 */
	public void printSeparador() {
		System.out.println(
				"-------------------------------------------------------------------------------------------------------------------");
	}

}
