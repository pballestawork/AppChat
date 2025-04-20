package dominio.controlador;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;
import dominio.repositorio.EntidadNoEncontrada;
import dominio.repositorio.IRepositorioUsuarios;
import dominio.repositorio.RepositorioException;
import dominio.repositorio.RepositorioString;
import dominio.repositorio.RepositorioUsuarios;
import persistencia.dao.DAOException;
import persistencia.dao.FactoriaDAO;
import persistencia.dao.IAdaptadorContactoIndividualDAO;
import persistencia.dao.IAdaptadorGrupoDAO;
import persistencia.dao.IAdaptadorMensajeDAO;
import persistencia.dao.IAdaptadorUsuarioDAO;

public class ChatController {
	private static ChatController unicaInstancia;
	private Usuario usuarioActual;

	private static IRepositorioUsuarios repositorioUsuarios;
	private static IAdaptadorUsuarioDAO usuarioDAO;
	private static IAdaptadorContactoIndividualDAO contactoIndividualDAO;
	private static IAdaptadorGrupoDAO grupoDAO;
	private static IAdaptadorMensajeDAO mensajeDAO;

	private ChatController() throws ChatControllerException {
		FactoriaDAO dao;
		try {
			dao = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			throw new ChatControllerException("Comprueba tu conexión con la base de datos.");
		}
		usuarioDAO = dao.getUsuarioDAO();
		contactoIndividualDAO = dao.getContactoIndividualDAO();
		grupoDAO = dao.getGrupoDAO();
		mensajeDAO = dao.getMensajeDAO();
		repositorioUsuarios = RepositorioUsuarios.getUnicaInstancia();
	}

	public static ChatController getUnicaInstancia() throws ChatControllerException {
		if (unicaInstancia == null) {
			unicaInstancia = new ChatController();
		}
		return unicaInstancia;
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	/**
	 * Registra un nuevo usuario en el sistema.
	 * 
	 * Si el número de teléfono ya está registrado, se muestra un mensaje de error.
	 */
	public void registrarUsuario(String nombre, LocalDateTime fechaNacimiento, String email, String fotoPerfil,
			String telefono, String contrasena, String saludo) throws RepositorioException {
	
		Usuario u = repositorioUsuarios.add(nombre, telefono, email, contrasena, fotoPerfil, saludo);
		usuarioDAO.add(u);
		repositorioUsuarios.add(u);
	}

	/**
	 * Inicia sesión en la aplicación verificando las credenciales del usuario.
	 *
	 * Esta función recibe el número de teléfono y la contraseña ingresados por el
	 * usuario, valida que coincidan con los datos de un usuario registrado en el
	 * sistema y, si son correctos, permite el acceso a la aplicación.
	 *
	 * Si las credenciales son válidas, se retorna el usuario correspondiente y se
	 * redirige a la pantalla principal de la aplicación. Si el número de teléfono
	 * no está registrado o la contraseña es incorrecta, se muestra un mensaje de
	 * error adecuado.
	 *
	 * @param telefono   Número de teléfono del usuario registrado.
	 * @param contrasena Contraseña del usuario.
	 * @return Objeto Usuario si las credenciales son correctas, o null si son
	 *         inválidas.
	 * @throws ChatControllerException
	 * @throws EntidadNoEncontrada
	 * @throws RepositorioException
	 */
	public Usuario iniciarSesion(String telefono, String contrasena)
			throws ChatControllerException, RepositorioException, EntidadNoEncontrada {

		Usuario u = repositorioUsuarios.getById(telefono);

		if (u == null || !u.getContrasena().equals(contrasena)) {
			throw new ChatControllerException("El telefono o la contraseña son incorrectos.");
		}

		usuarioActual = u;
		return u;
	}

	/**
	 * Actualiza el saludo y/o la imagen de perfil del usuario.
	 *
	 * @param nuevoSaludo Nuevo saludo del usuario (puede ser nulo si no se quiere
	 *                    actualizar).
	 * @param nuevaImagen Nueva imagen de perfil del usuario (puede ser nula si no
	 *                    se quiere actualizar).
	 * @throws EntidadNoEncontrada
	 * @throws RepositorioException
	 */
	public void actualizarPerfil(String nuevoSaludo, String nuevaImagen)
			throws RepositorioException, EntidadNoEncontrada {
		if (nuevoSaludo == null && nuevaImagen == null) {
			throw new IllegalArgumentException("Debe proporcionar al menos un campo para actualizar.");
		}

		if (nuevoSaludo != null) {
			usuarioActual.setSaludo(nuevoSaludo);
		}

		if (nuevaImagen != null) {
			usuarioActual.setFotoPerfil(nuevaImagen);
		}

		usuarioDAO.update(usuarioActual);
	}

	/**
	 * Añade un nuevo contacto a la lista de contactos del usuario.
	 *
	 * Esta función permite a un usuario registrado agregar un contacto
	 * proporcionando un número de teléfono y un nombre asociado.
	 *
	 * Se realizan las siguientes validaciones: - Verifica que el número de teléfono
	 * no esté ya en la lista de contactos del usuario. - Comprueba que el número de
	 * teléfono existe en el sistema.
	 *
	 * Si el número no existe en el sistema, se muestra un mensaje de error. Si la
	 * adición es exitosa, el contacto se guarda y se muestra en la lista de
	 * contactos del usuario.
	 *
	 * @param numeroTelefono Número de teléfono del contacto a agregar.
	 * @param nombre         Nombre asociado al contacto.
	 * @throws ChatControllerException
	 * @throws EntidadNoEncontrada
	 * @throws RepositorioException
	 */
	public void agregarContacto(String nombre, String telefono)
			throws ChatControllerException, RepositorioException, EntidadNoEncontrada {

		if (usuarioActual.tieneContactoConTelefono(telefono)) {
			throw new ChatControllerException("Ya tienes un contacto asociado a este numero.");
		} 
		Usuario usuarioDestino = repositorioUsuarios.getById(telefono);
		if (usuarioDestino == null) {
			throw new ChatControllerException("El numero " + telefono + " no existe.");
		}
		
		// Usar el método de Usuario para crear el contacto (patrón GRASP Creador)
		ContactoIndividual nuevoContacto = usuarioActual.crearContactoIndividual(nombre, usuarioDestino);
		
		// Persistir cambios
		contactoIndividualDAO.add(nuevoContacto);
		usuarioDAO.update(usuarioActual);
	}

	/**
	 * Elimina un contacto o grupo de la lista de contactos del usuario actual.
	 * 
	 * Se realizan las siguientes validaciones: - Verifica si el contacto está en la
	 * lista de contactos del usuario actual. - Si el contacto no existe en la
	 * lista, lanza una excepción.
	 * 
	 * @param contacto Contacto o grupo a eliminar.
	 * @throws ChatControllerException Si el contacto no pertenece a la lista de
	 *                                 contactos del usuario.
	 */
	public void eliminarContacto(Contacto contacto) throws ChatControllerException {

		// Verificar si el contacto está en la lista del usuario actual
		if (!usuarioActual.getContactos().contains(contacto)) {
			throw new ChatControllerException("El contacto o grupo no está en la lista de contactos del usuario.");
		}

		// Eliminar el contacto de la lista
		usuarioActual.deleteContacto(contacto);
		if (contacto instanceof ContactoIndividual) {
			contactoIndividualDAO.delete((ContactoIndividual) contacto);
		} else {
			grupoDAO.delete((Grupo) contacto);
		}
		usuarioDAO.update(usuarioActual);
	}

	/**
	 * Crea un nuevo grupo de contactos.
	 *
	 * Esta función permite al usuario crear un grupo proporcionando un nombre y,
	 * opcionalmente, una imagen. Además, puede añadir varios contactos existentes
	 * al grupo.
	 *
	 * Se realizan las siguientes validaciones: - El nombre del grupo no puede estar
	 * vacío. - Solo se pueden agregar contactos que ya existan en la lista del
	 * usuario.
	 *
	 * Si la creación es exitosa, el grupo se añade a la lista de contactos del
	 * usuario.
	 *
	 * @param miembros
	 * @param nombreGrupo
	 * @throws ChatControllerException
	 */
	public void crearGrupo(List<ContactoIndividual> miembros, String nombreGrupo, String imagenGrupo)
			throws ChatControllerException {

		Map<String, ContactoIndividual> contactosMap = usuarioActual.getContactos().stream()
				.filter(c -> c instanceof ContactoIndividual).map(c -> (ContactoIndividual) c)
				.collect(Collectors.toMap(c -> c.getUsuario().getTelefono(), c -> c));

		for (ContactoIndividual contactoIndividual : miembros) {
			if (!contactosMap.containsKey(contactoIndividual.getUsuario().getTelefono())) {
				throw new ChatControllerException(
						"El contacto con numero " + contactoIndividual.getUsuario().getTelefono() + " no existe.");
			}
		}

		Grupo nuevoGrupo = usuarioActual.crearGrupo(nombreGrupo, miembros, imagenGrupo);
		
		grupoDAO.add(nuevoGrupo);
		usuarioDAO.update(usuarioActual);
	}

	/**
	 * Añade un contacto individual a un grupo existente.
	 *
	 * Se realizan las siguientes validaciones: - El grupo y el contacto no pueden
	 * ser nulos. - El grupo debe existir en la lista de contactos del usuario. - El
	 * contacto debe estar en la lista de contactos del usuario antes de ser
	 * agregado al grupo. - El contacto no debe estar ya en el grupo.
	 *
	 * @param grupo              Grupo al que se añadirá el contacto.
	 * @param contactoIndividual Contacto individual a añadir.
	 * @throws ChatControllerException Si el grupo no pertenece al usuario o el
	 *                                 contacto no está en su lista.
	 */
	public void agregarContactoAGrupo(Grupo grupo, ContactoIndividual contactoIndividual)// TODO lista mejor?? //TODO
																							// mejor usar ids?
			throws ChatControllerException {

		// Verificar si el grupo pertenece a la lista de contactos del usuario actual
		boolean grupoExiste = usuarioActual.getContactos().stream()
				.anyMatch(contacto -> contacto instanceof Grupo && contacto.equals(grupo));

		if (!grupoExiste) {
			throw new ChatControllerException("El grupo no pertenece a la lista de contactos del usuario.");
		}

		// Verificar si el contacto existe en la lista de contactos del usuario actual
		boolean contactoExiste = usuarioActual.getContactos().stream().filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c).anyMatch(c -> c.equals(contactoIndividual));

		if (!contactoExiste) {
			throw new ChatControllerException("El contacto no pertenece a la lista de contactos del usuario.");
		}

		// Verificar que el contacto no esté ya en el grupo
		if (grupo.getMiembros().contains(contactoIndividual)) {
			throw new ChatControllerException("El contacto ya está en el grupo.");
		}

		// Agregar el contacto al grupo
		grupo.addMiembro(contactoIndividual);
		grupoDAO.update(grupo);
	}

	/**
	 * Elimina un contacto individual de un grupo existente.
	 *
	 * Se realizan las siguientes validaciones: - El grupo y el contacto no pueden
	 * ser nulos. - El grupo debe existir en la lista de contactos del usuario. - El
	 * contacto debe estar en el grupo antes de poder eliminarlo.
	 *
	 * @param grupo              Grupo del que se eliminará el contacto.
	 * @param contactoIndividual Contacto individual a eliminar del grupo.
	 * @throws ChatControllerException Si el grupo no pertenece al usuario o el
	 *                                 contacto no está en el grupo.
	 */
	public void eliminarContactoDeGrupo(Grupo grupo, ContactoIndividual contactoIndividual)
			throws ChatControllerException {

		// Verificar si el grupo pertenece a la lista de contactos del usuario actual
		boolean grupoExiste = usuarioActual.getContactos().stream()
				.anyMatch(contacto -> contacto instanceof Grupo && contacto.equals(grupo));

		if (!grupoExiste) {
			throw new ChatControllerException("El grupo no pertenece a la lista de contactos del usuario.");
		}

		// Verificar que el contacto esté en el grupo antes de eliminarlo
		if (!grupo.getMiembros().contains(contactoIndividual)) {
			throw new ChatControllerException("El contacto no está en el grupo.");
		}

		// Eliminar el contacto del grupo
		grupo.deleteMiembro(contactoIndividual);
		grupoDAO.update(grupo);
	}

	/**
	 * Actualiza el nombre y/o la imagen de un grupo en la lista de contactos del
	 * usuario actual.
	 * 
	 * Se realizan las siguientes validaciones: - El grupo no puede ser nulo. - El
	 * grupo debe existir en la lista de contactos del usuario. - Si el nuevo nombre
	 * es proporcionado, se actualiza. - Si la nueva imagen es proporcionada, se
	 * actualiza. - Si ambos valores son nulos, no se realiza ninguna actualización.
	 *
	 * @param grupo  Grupo a actualizar.
	 * @param nombre Nuevo nombre del grupo (puede ser nulo si no se desea
	 *               actualizar).
	 * @param imagen Nueva imagen del grupo (puede ser nula si no se desea
	 *               actualizar).
	 * @throws ChatControllerException Si el grupo no pertenece a la lista de
	 *                                 contactos del usuario.
	 */
	public void actualizarGrupo(Grupo grupo, String nombre, String imagen) throws ChatControllerException {
		if (nombre == null && imagen == null) {
			throw new IllegalArgumentException("Debe proporcionar al menos un campo para actualizar.");
		} else if (!usuarioActual.getContactos().contains(grupo)) {
			throw new ChatControllerException("El grupo no pertenece a la lista de contactos del usuario.");
		}

		if (nombre != null) {
			grupo.setNombre(nombre);
		}

		if (imagen != null) {
			grupo.setImagen(imagen);
		}

		grupoDAO.update(grupo);
	}

	/**
	 * Envía un mensaje a un contacto, ya sea un contacto individual o un grupo.
	 *
	 * Se realizan las siguientes validaciones: - El contacto debe existir en la
	 * lista de contactos del usuario actual.
	 *
	 * El mensaje se guarda en la lista de mensajes del contacto.
	 *
	 * @param contacto  Destinatario del mensaje (puede ser ContactoIndividual o
	 *                  Grupo).
	 * @param contenido Contenido del mensaje.
	 * @throws ChatControllerException Si el contacto no está en la lista del
	 *                                 usuario actual.
	 */
	public void enviarMensaje(Contacto contacto, String contenido) throws ChatControllerException {
		if (contacto == null) {
			throw new IllegalArgumentException("El contacto no puede ser nulo.");
		}
		if (contenido == null || contenido.trim().isEmpty()) {
			throw new IllegalArgumentException("El contenido del mensaje no puede estar vacío.");
		}

		Contacto contactoRecuperado = usuarioActual.getContactos().stream().filter(c -> c.equals(contacto)).findFirst()
				.orElse(null);

		if (contactoRecuperado == null) {
			throw new ChatControllerException("El contacto no está en la lista del usuario actual.");
		}

		Mensaje mensaje = null;//TODO new Mensaje(0, usuarioActual, contenido, LocalDateTime.now(), true);
		if (contactoRecuperado instanceof ContactoIndividual) {
			enviarMensajeContactoIndividual((ContactoIndividual) contactoRecuperado, mensaje);
		} else if (contactoRecuperado instanceof Grupo) {
			enviarMensajeGrupo((Grupo) contactoRecuperado, mensaje);
		}
	}

	private void enviarMensajeContactoIndividual(ContactoIndividual contacto, Mensaje m) {
		// que pasa si el otro no me tiene agregado como contacto??
		ContactoIndividual miContactoEnElReceptor = contacto.getUsuario().getContactos().stream()
				.filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c)
				.filter(c -> c.getUsuario().equals(usuarioActual))
				.findFirst().orElse(null);
		
		if(miContactoEnElReceptor == null) {
			//Crear mi contacto con el nombre = telefono
			miContactoEnElReceptor = new ContactoIndividual(0, usuarioActual.getTelefono(), usuarioActual);
			contacto.getUsuario().addContacto(miContactoEnElReceptor);
			contactoIndividualDAO.add(miContactoEnElReceptor);
			usuarioDAO.update(contacto.getUsuario());
		}
		
		Mensaje copia = m.clone();
		copia.setTipo(false);
		miContactoEnElReceptor.addMensaje(copia);
		
		mensajeDAO.add(copia);
		contactoIndividualDAO.update(miContactoEnElReceptor);
	
		//añadirlo a mi contacto/chat
		contacto.addMensaje(m);
		mensajeDAO.add(m);
		contactoIndividualDAO.update(contacto);
	}

	private void enviarMensajeGrupo(Grupo grupo, Mensaje m) {
		grupo.addMensaje(m);
		mensajeDAO.update(m);
		grupoDAO.update(grupo);

		for (ContactoIndividual contactoInd : grupo.getMiembros()) {
			enviarMensajeContactoIndividual(contactoInd, m.clone());
		}
	}

	/**
	 * Busca mensajes en la lista de contactos del usuario aplicando filtros
	 * opcionales por fragmento de texto, nombre del contacto o número de teléfono.
	 * 
	 * Se realizan las siguientes validaciones: - Se pueden combinar múltiples
	 * criterios de búsqueda. - Se buscan mensajes en la lista de contactos del
	 * usuario, ya sean enviados o recibidos. - Los resultados se ordenan por fecha
	 * y hora de envío de manera descendente.
	 * 
	 * @param texto    Fragmento de texto a buscar en los mensajes (puede ser nulo).
	 * @param contacto Nombre del contacto a filtrar (puede ser nulo).
	 * @param telefono Número de teléfono del contacto a filtrar (puede ser nulo).
	 * @return Lista de mensajes que coincidan con los criterios de búsqueda.
	 */
	public List<Mensaje> buscarMensajes(String texto, String contacto, String telefono) {
		// Obtener todos los mensajes de todos los contactos
		Stream<Mensaje> mensajes = usuarioActual.getContactos().stream()
				.flatMap(contactoObj -> contactoObj.getMensajes().stream());
		
		// Aplicar filtros si están especificados
		mensajes = aplicarFiltrosBusqueda(mensajes, texto, contacto, telefono);
		
		// Ordenar por fecha de envío (más reciente primero)
		return mensajes.sorted(Comparator.comparing(Mensaje::getFechaEnvio).reversed())
				.collect(Collectors.toList());
	}
	
	/**
	 * Aplica filtros a un stream de mensajes según los criterios especificados
	 * 
	 * @param mensajes Stream de mensajes a filtrar
	 * @param texto Texto a buscar en el contenido (puede ser nulo)
	 * @param contacto Nombre del contacto a filtrar (puede ser nulo)
	 * @param telefono Teléfono del contacto a filtrar (puede ser nulo)
	 * @return Stream filtrado según los criterios
	 */
	private Stream<Mensaje> aplicarFiltrosBusqueda(Stream<Mensaje> mensajes, String texto, String contacto, String telefono) {
		// Filtrar por texto en el contenido si se especifica
		if (texto != null && !texto.trim().isEmpty()) {
			mensajes = mensajes.filter(mensaje -> mensaje.contienePalabra(texto));
		}
		
		// Filtrar por nombre de contacto si se especifica
		if (contacto != null && !contacto.trim().isEmpty()) {
			mensajes = mensajes.filter(mensaje -> mensaje.getEmisor().getNombre().equalsIgnoreCase(contacto));
		}
		
		// Filtrar por número de teléfono si se especifica
		if (telefono != null && !telefono.trim().isEmpty()) {
			mensajes = mensajes.filter(mensaje -> mensaje.getEmisor().getTelefono().equals(telefono));
		}
		
		return mensajes;
	}

	public void exportarMensajesPDF(Usuario usuario, List<Mensaje> mensajes) {
		return;
	}

	public void cerrarSesion() {
		return;
	}
}