package dominio.controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;
import dominio.modelo.Descuento;
import dominio.repositorio.EntidadNoEncontrada;
import dominio.repositorio.IRepositorioUsuarios;
import dominio.repositorio.RepositorioException;
import dominio.repositorio.RepositorioUsuarios;
import persistencia.dao.DAOException;
import persistencia.dao.FactoriaDAO;
import persistencia.dao.IAdaptadorContactoIndividualDAO;
import persistencia.dao.IAdaptadorGrupoDAO;
import persistencia.dao.IAdaptadorMensajeDAO;
import persistencia.dao.IAdaptadorUsuarioDAO;
import utils.DescuentoFactory;

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
	public void registrarUsuario(String nombre, LocalDate fechaNacimiento, String email, String fotoPerfil,
			String telefono, String contrasena, String saludo) throws RepositorioException {
	
		Usuario u = repositorioUsuarios.add(nombre, telefono, email, contrasena, fotoPerfil, saludo, fechaNacimiento);
		usuarioDAO.add(u);
	}

	/**
	 * Inicia sesión en la aplicación verificando las credenciales del usuario.
	 *
	 * Se valida que el número de teléfono no está registrado o la contraseña 
	 * es incorrecta. Si las credenciales son válidas, se retorna el usuario correspondiente. 
	 * @throws RepositorioException 
	 */
	public Usuario iniciarSesion(String telefono, String contrasena)
			throws ChatControllerException, EntidadNoEncontrada, RepositorioException {

		Usuario u = repositorioUsuarios.getById(telefono);
		
		if (!u.validarContrasena(contrasena))
			throw new ChatControllerException("El telefono o la contraseña son incorrectos.");
		

		usuarioActual = u;
		return u;
	}

	/**
	 * Actualiza el saludo y/o la imagen de perfil del usuario.
	 */
	public void actualizarPerfil(String nuevoSaludo, String nuevaImagen)
			throws RepositorioException, EntidadNoEncontrada {
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
	 */
	public void agregarContacto(String nombre, String telefono)
			throws ChatControllerException, RepositorioException, EntidadNoEncontrada {

		Usuario usuarioDestino = repositorioUsuarios.getById(telefono);
		if (usuarioDestino == null) {
			throw new ChatControllerException("El numero " + telefono + " no existe.");
		}
		
		if (usuarioActual.tieneContactoConTelefono(telefono)) {
			throw new ChatControllerException("Ya tienes un contacto asociado a este numero.");
		} 
		
		ContactoIndividual nuevoContacto = usuarioActual.addContactoIndividual(nombre, usuarioDestino);
		
		contactoIndividualDAO.add(nuevoContacto);
		usuarioDAO.update(usuarioActual);
	}

	/**
	 * Elimina un contacto o grupo de la lista de contactos del usuario actual.
	 */
	public void eliminarContacto(Contacto contacto) throws ChatControllerException {

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
	 * Actualiza el saludo y/o la imagen de perfil del usuario.
	 */
	public void actualizarContacto(Contacto contacto, String nombre){
		
		contacto.setNombre(nombre);
		
		if (contacto instanceof ContactoIndividual) {
			contactoIndividualDAO.update((ContactoIndividual) contacto);
		} else {
			grupoDAO.update((Grupo) contacto);
		}
	}

	/**
	 * Crea un nuevo grupo de contactos.
	 */
	public void crearGrupo(List<ContactoIndividual> miembros, String nombreGrupo, String imagenGrupo)
			throws ChatControllerException {

		Grupo nuevoGrupo = usuarioActual.addGrupo(nombreGrupo, miembros, imagenGrupo);
		
		grupoDAO.add(nuevoGrupo);
		usuarioDAO.update(usuarioActual);
	}

	/**
	 * Añade un contacto individual a un grupo existente.
	 */
	public void agregarContactoAGrupo(Grupo grupo, ContactoIndividual contactoIndividual)
			throws ChatControllerException {

		// Verificar que el contacto no esté ya en el grupo //TODO Comprobar en front
		if (grupo.getMiembros().contains(contactoIndividual)) {
			throw new ChatControllerException("El contacto ya está en el grupo.");
		}

		// Agregar el contacto al grupo
		grupo.addMiembro(contactoIndividual);
		grupoDAO.update(grupo);
	}

	/**
	 * Elimina un contacto individual de un grupo existente.
	 */
	public void eliminarContactoDeGrupo(Grupo grupo, ContactoIndividual contactoIndividual)
			throws ChatControllerException {

		// Verificar que el contacto esté en el grupo antes de eliminarlo //TODO comprobar en front
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
	 */
	public void actualizarGrupo(Grupo grupo, String nombre, String imagen) throws ChatControllerException {

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
	 */
	public void enviarMensaje(Contacto contacto, String contenido) throws ChatControllerException {	
		if (contacto instanceof ContactoIndividual) {
			enviarMensajeContactoIndividual((ContactoIndividual) contacto, contenido);
		} else if (contacto instanceof Grupo) {
			enviarMensajeGrupo((Grupo) contacto, contenido);
		}
	}

	private void enviarMensajeContactoIndividual(ContactoIndividual contacto, String contenido) {
		//añadirlo a mi contacto/chat
		Mensaje mensajeEmisor = contacto.addMensaje(usuarioActual, contenido);
		mensajeDAO.add(mensajeEmisor);
		contactoIndividualDAO.update(contacto);
		
		// que pasa si el otro soy yo??
		if(!contacto.perteneceaUsuarioConTelefono(usuarioActual.getTelefono())) {
			ContactoIndividual miContactoEnElReceptor = contacto.contactoEnElReceptor(usuarioActual.getTelefono());

			// que pasa si el otro no me tiene agregado como contacto??
			if(miContactoEnElReceptor == null ) {
				miContactoEnElReceptor = contacto.crearContactoEnElReceptor(usuarioActual);
				contactoIndividualDAO.add(miContactoEnElReceptor);
				usuarioDAO.update(miContactoEnElReceptor.getUsuario());
			}
			
			Mensaje mensajeReceptor = miContactoEnElReceptor.addMensaje(usuarioActual, contenido);	
			mensajeDAO.add(mensajeReceptor);
			contactoIndividualDAO.update(miContactoEnElReceptor);			
		}
		
	}

	private void enviarMensajeGrupo(Grupo grupo, String contenido) {
		Mensaje m = grupo.addMensaje(usuarioActual,contenido);
		mensajeDAO.add(m);
		grupoDAO.update(grupo);

		for (ContactoIndividual contactoInd : grupo.getMiembros()) {
			enviarMensajeContactoIndividual(contactoInd, contenido);
		}
	}

	/**
	 * Busca mensajes en la lista de contactos del usuario aplicando filtros
	 * opcionales por fragmento de texto, nombre del contacto o número de teléfono.
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

	public void cerrarSesion() {
			this.usuarioActual = null;
	}
	
	/**
	 * Actualiza el estado premium del usuario actual.
	 * En una implementación real, este método también realizaría la integración con un sistema de pagos.
	 */
	public boolean actualizarUsuarioAPremium() {
		if (usuarioActual.isEsPremium()) {
			return true;
		}
		
		try {
			usuarioActual.convertirAPremium();
			usuarioDAO.update(usuarioActual);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Genera un informe PDF con los contactos y grupos del usuario actual.
	 * Esta funcionalidad solo está disponible para usuarios premium.
	 * 
	 * @param rutaDestino Ruta donde se guardará el archivo PDF
	 * @return true si el PDF se generó correctamente, false en caso contrario
	 */
	public boolean generarInformePDF(String rutaDestino) {
		if (usuarioActual == null) {
			return false;
		}
		
		// Verificar que el usuario es premium
		if (!usuarioActual.isEsPremium()) {
			return false;
		}
		
		// Utilizar el generador de PDF
		return utils.PDFGenerator.generarInformeContactos(usuarioActual, rutaDestino);
	}
	
	/**
	 * Verifica si un descuento es válido para el usuario actual.
	 * 
	 * @param descuento El descuento a validar
	 * @return true si el usuario puede usar el descuento, false en caso contrario
	 */
	public boolean validarDescuentoParaUsuario(Descuento descuento) {
		if (usuarioActual == null || descuento == null) {
			return false;
		}
		
		// Obtener la fecha de nacimiento del usuario
		LocalDate fechaNacimiento = usuarioActual.getFechaNacimiento();
		
		// Configurar el contexto del usuario en el descuento
		DescuentoFactory.configurarContextoUsuario(descuento, fechaNacimiento);
		
		// Validar si el descuento es aplicable
		return descuento.esAplicable();
	}
}