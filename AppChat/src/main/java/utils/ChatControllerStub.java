package utils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dominio.controlador.ChatControllerException;
import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;

public class ChatControllerStub {	
	private static ChatControllerStub unicaInstancia;
	
	private Usuario usuarioActual;
	private Map<String,Usuario> usuarios;
	
	
	private ChatControllerStub () {
		usuarioActual = null; 
		
		Usuario usuario = new Usuario(1,"Pablo","1","pablo@gmail.com","123","/FotosPerfil/Perfil_2.png",false,"Hola",new LinkedList<Contacto>());
		Usuario usuario2 = new Usuario(2,"Alvaro","2","alvaro@gmail.com","123","/FotosPerfil/Perfil_1.png",false,"Hola",new LinkedList<Contacto>());
		Usuario usuario3 = new Usuario(3,"Laura","3","laura@gmail.com","123","/FotosPerfil/Perfil_3.png",false,"Hola",new LinkedList<Contacto>());
		Usuario usuario4 = new Usuario(3,"Pepe","4","pepe@gmail.com","123","/FotosPerfil/Perfil_2.png",false,"Hola,soy pepe",new LinkedList<Contacto>());
		
		usuarios = new HashMap<String, Usuario>();
		usuarios.put(usuario.getTelefono(),usuario);
		usuarios.put(usuario2.getTelefono(),usuario2);
		usuarios.put(usuario3.getTelefono(),usuario3);
		usuarios.put(usuario4.getTelefono(),usuario4);
		
		ContactoIndividual alvaroDePablo = FactoriaPruebas.crearContactoIndividual(usuario2);
		ContactoIndividual lauraDePablo = FactoriaPruebas.crearContactoIndividual(usuario3);
		ContactoIndividual pepeDePablo = FactoriaPruebas.crearContactoIndividual(usuario4);
		pepeDePablo.setNombre("");
		
		ContactoIndividual pabloDeAlvaro = FactoriaPruebas.crearContactoIndividual(usuario);
		ContactoIndividual lauraDeAlvaro = FactoriaPruebas.crearContactoIndividual(usuario3);
		
		ContactoIndividual pabloDeLaura = FactoriaPruebas.crearContactoIndividual(usuario);
		ContactoIndividual alvaroDeLaura = FactoriaPruebas.crearContactoIndividual(usuario2);
		
		usuario.addContacto(alvaroDePablo);
		usuario.addContacto(lauraDePablo);
		usuario.addContacto(pepeDePablo);
		
		usuario2.addContacto(pabloDeAlvaro);
		usuario2.addContacto(lauraDeAlvaro);
		
		usuario3.addContacto(pabloDeLaura);
		usuario3.addContacto(alvaroDeLaura);
	
		alvaroDePablo.addMensaje(new Mensaje(1, usuario, "Hola", LocalDateTime.now(), true));
		alvaroDePablo.addMensaje(new Mensaje(2, usuario, "Como estas Alvaro?", LocalDateTime.now(), true));
		alvaroDePablo.addMensaje(new Mensaje(3, usuario2, "Hola", LocalDateTime.now(), false));
		alvaroDePablo.addMensaje(new Mensaje(4, usuario2, "Bien y tu?", LocalDateTime.now(), false));
		alvaroDePablo.addMensaje(new Mensaje(5, usuario, "Bien gracias", LocalDateTime.now(), true));
		
		pabloDeAlvaro.addMensaje(new Mensaje(1, usuario, "Hola", LocalDateTime.now(), false));
		pabloDeAlvaro.addMensaje(new Mensaje(2, usuario, "Como estas Alvaro?", LocalDateTime.now(), false));
		pabloDeAlvaro.addMensaje(new Mensaje(3, usuario2, "Hola", LocalDateTime.now(), true));
		pabloDeAlvaro.addMensaje(new Mensaje(4, usuario2, "Bien y tu?", LocalDateTime.now(), true));
		pabloDeAlvaro.addMensaje(new Mensaje(5, usuario, "Bien gracias", LocalDateTime.now(), false));
		
		lauraDePablo.addMensaje(new Mensaje(3, usuario, "Hola", LocalDateTime.now(), true));
		lauraDePablo.addMensaje(new Mensaje(4, usuario, "Como estas Laura?", LocalDateTime.now(), true));
		
		pabloDeLaura.addMensaje(new Mensaje(3, usuario, "Hola", LocalDateTime.now(), false));
		pabloDeLaura.addMensaje(new Mensaje(4, usuario, "Como estas Laura?", LocalDateTime.now(), false));
		
	}
	
	public static ChatControllerStub getUnicaInstancia() {
		if(unicaInstancia == null)
			unicaInstancia = new ChatControllerStub();
		return unicaInstancia;
	}
	
	
	/**
	 * Registra un nuevo usuario en el sistema.
	 *
	 * Esta función recibe los datos ingresados por el usuario en el formulario de registro,
	 * valida que los campos obligatorios estén completos y que las contraseñas coincidan.
	 * 
	 * Si el número de teléfono ya está registrado, se muestra un mensaje de error.
	 * En caso de que el registro sea exitoso, el usuario recibe una confirmación y es 
	 * redirigido a la página de inicio de sesión.
	 * 
	 * @param nombre
	 * @param fechaNacimiento
	 * @param email
	 * @param fotoPerfil
	 * @param telefono
	 * @param contrasena
	 * @param contrasenaRepetida
	 * @throws IllegalArgumentException
	 * @throws ChatControllerException 
	 */
	public void registrarUsuario(String nombre, LocalDateTime fechaNacimiento, String email, String fotoPerfil,
			String telefono, String contrasena, String contrasenaRepetida, String saludo) throws ChatControllerException {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacio.");
		} else if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("El email no puede ser nulo o vacio.");
		} else if (telefono == null || telefono.trim().isEmpty()) {
			throw new IllegalArgumentException("El telefono no puede ser nulo o vacio.");
		} else if (contrasena == null || contrasena.trim().isEmpty()) {
			throw new IllegalArgumentException("La contraseña no puede ser nulo o vacio.");
		} else if (contrasenaRepetida == null || !contrasenaRepetida.equals(contrasena)) {
			throw new IllegalArgumentException("Las contraseñas no coinciden");
		} else if(fechaNacimiento != null && fechaNacimiento.isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException("La fecha de nacimiento no puede ser posterior a la fecha actual.");
		}
		
		if(usuarios.containsKey(telefono)) {
			throw new ChatControllerException("El telefono "+ telefono +" ya está registrado.");
		}
		
		Usuario u = new Usuario(0, nombre, telefono, email,contrasena, fotoPerfil,false,saludo,new LinkedList<Contacto>());
		usuarios.put(telefono, u);
	}
	
	/**
	 * Inicia sesión en la aplicación verificando las credenciales del usuario.
	 *
	 * Esta función recibe el número de teléfono y la contraseña ingresados por el usuario, 
	 * valida que coincidan con los datos de un usuario registrado en el sistema y, si son 
	 * correctos, permite el acceso a la aplicación.
	 *
	 * Si las credenciales son válidas, se retorna el usuario correspondiente y se redirige 
	 * a la pantalla principal de la aplicación.
	 * Si el número de teléfono no está registrado o la contraseña es incorrecta, se muestra 
	 * un mensaje de error adecuado.
	 *
	 * @param telefono Número de teléfono del usuario registrado.
	 * @param contrasena Contraseña del usuario.
	 * @return Objeto Usuario si las credenciales son correctas, o null si son inválidas.
	 * @throws ChatControllerException 
	 */
	public Usuario iniciarSesion(String telefono, String contrasena) throws ChatControllerException {
		if (telefono == null || telefono.trim().isEmpty()) {
			throw new IllegalArgumentException("El telefono no puede ser nulo o vacio.");
		} else if (contrasena == null || contrasena.trim().isEmpty()) {
			throw new IllegalArgumentException("La contraseña no puede ser nula o vacia.");
		}
		
		Usuario u = usuarios.get(telefono);
		
		if(u == null || !u.getContrasena().equals(contrasena)) {
			throw new ChatControllerException("El telefono o la contraseña son incorrectos.");
		}
		
		usuarioActual = u;
		return u;
	}
	
	public Usuario getUsuarioActual() {
	    return usuarioActual; 
	}
	
	/**
	 * Actualiza el saludo y/o la imagen de perfil del usuario.
	 *
	 * @param nuevoSaludo Nuevo saludo del usuario (puede ser nulo si no se quiere actualizar).
	 * @param nuevaImagen Nueva imagen de perfil del usuario (puede ser nula si no se quiere actualizar).
	 */
	public void actualizarPerfil(String nuevoSaludo, String nuevaImagen) {
	    if (nuevoSaludo == null && nuevaImagen == null) {
	        throw new IllegalArgumentException("Debe proporcionar al menos un campo para actualizar.");
	    }

	    if (nuevoSaludo != null) {
	        usuarioActual.setSaludo(nuevoSaludo);
	    }

	    if (nuevaImagen != null) {
	        usuarioActual.setFotoPerfil(nuevaImagen);
	    }
	}
	
	/**
	 * Añade un nuevo contacto a la lista de contactos del usuario.
	 *
	 * Esta función permite a un usuario registrado agregar un contacto proporcionando 
	 * un número de teléfono y un nombre asociado. 
	 *
	 * Se realizan las siguientes validaciones:
	 * - Verifica que el número de teléfono no esté ya en la lista de contactos del usuario.
	 * - Comprueba que el número de teléfono existe en el sistema.
	 *
	 * Si el número no existe en el sistema, se muestra un mensaje de error.
	 * Si la adición es exitosa, el contacto se guarda y se muestra en la lista de contactos del usuario.
	 *
	 * @param numeroTelefono Número de teléfono del contacto a agregar.
	 * @param nombre Nombre asociado al contacto.
	 * @throws ChatControllerException 
	 */
	public void agregarContacto(String nombre, String telefono) throws ChatControllerException {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacio.");
		} else if (telefono == null || telefono.trim().isEmpty()) {
			throw new IllegalArgumentException("El telefono no puede ser nulo o vacio.");
		}
		 
		List<Contacto> contactos = usuarioActual.getContactos();
		boolean contactoYaRegistrado = contactos.stream()
	            .filter(contacto -> contacto instanceof ContactoIndividual) 
	            .map(contacto -> (ContactoIndividual) contacto) 
	            .map(ContactoIndividual::getUsuario) 
	            .anyMatch(usuario -> usuario.getTelefono().equals(telefono));
		
		if(contactoYaRegistrado){
			throw new ChatControllerException("Ya tienes un contacto asociado a este numero.");
		}else if(!usuarios.containsKey(telefono)) {
			throw new ChatControllerException("El numero "+ telefono +" no existe.");
		}

		ContactoIndividual c = new ContactoIndividual(usuarios.get(telefono).getId(), nombre, usuarios.get(telefono));
		usuarioActual.addContacto(c);
	}
	
	/**
	 * Elimina un contacto o grupo de la lista de contactos del usuario actual.
	 * 
	 * Se realizan las siguientes validaciones:
	 * - Verifica si el contacto está en la lista de contactos del usuario actual.
	 * - Si el contacto no existe en la lista, lanza una excepción.
	 * 
	 * @param contacto Contacto o grupo a eliminar.
	 * @throws ChatControllerException Si el contacto no pertenece a la lista de contactos del usuario.
	 */
	public void eliminarContacto(Contacto contacto) throws ChatControllerException {
	    if (contacto == null) {
	        throw new IllegalArgumentException("El contacto no puede ser nulo.");
	    }

	    // Verificar si el contacto está en la lista del usuario actual
	    if (!usuarioActual.getContactos().contains(contacto)) {
	        throw new ChatControllerException("El contacto o grupo no está en la lista de contactos del usuario.");
	    }

	    // Eliminar el contacto de la lista
	    usuarioActual.deleteContacto(contacto);
	}
	
	
	
	
	/**
	 * Crea un nuevo grupo de contactos.
	 *
	 * Esta función permite al usuario crear un grupo proporcionando un nombre y, opcionalmente, 
	 * una imagen. Además, puede añadir varios contactos existentes al grupo.
	 *
	 * Se realizan las siguientes validaciones:
	 * - El nombre del grupo no puede estar vacío.
	 * - Solo se pueden agregar contactos que ya existan en la lista del usuario.
	 *
	 * Si la creación es exitosa, el grupo se añade a la lista de contactos del usuario.
	 *
	 * @param miembros
	 * @param nombreGrupo
	 * @throws ChatControllerException 
	 */
	public void crearGrupo(List<ContactoIndividual> miembros,String nombreGrupo, String imagenGrupo) throws ChatControllerException {
		if(nombreGrupo == null || nombreGrupo.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del grupo no puede ser nulo o vacío.");
		}else if(miembros == null || miembros.isEmpty()) {
			throw new IllegalArgumentException("La lista de miembros no puede ser nula o vacia.");
		}
		
		Map<String, ContactoIndividual> contactosMap = usuarioActual.getContactos().stream()
		        .filter(c -> c instanceof ContactoIndividual)
		        .map(c -> (ContactoIndividual) c)
		        .collect(Collectors.toMap(
		                c -> c.getUsuario().getTelefono(), 
		                c -> c 
		        ));
		
		for (ContactoIndividual contactoIndividual : miembros) {
			if(!contactosMap.containsKey(contactoIndividual.getUsuario().getTelefono())) {
				throw new ChatControllerException("El contacto con numero "+ contactoIndividual.getUsuario().getTelefono() +" no existe.");
			}
		}
		
		Grupo g = new Grupo(0, nombreGrupo, miembros, imagenGrupo);
		usuarioActual.addContacto(g);
	}

	
	/**
	 * Añade un contacto individual a un grupo existente.
	 *
	 * Se realizan las siguientes validaciones:
	 * - El grupo y el contacto no pueden ser nulos.
	 * - El grupo debe existir en la lista de contactos del usuario.
	 * - El contacto debe estar en la lista de contactos del usuario antes de ser agregado al grupo.
	 * - El contacto no debe estar ya en el grupo.
	 *
	 * @param grupo Grupo al que se añadirá el contacto.
	 * @param contactoIndividual Contacto individual a añadir.
	 * @throws ChatControllerException Si el grupo no pertenece al usuario o el contacto no está en su lista.
	 */
	public void agregarContactoAGrupo(Grupo grupo, ContactoIndividual contactoIndividual) throws ChatControllerException {
	    if (grupo == null) {
	        throw new IllegalArgumentException("El grupo no puede ser nulo.");
	    }
	    if (contactoIndividual == null) {
	        throw new IllegalArgumentException("El contacto no puede ser nulo.");
	    }

	    // Verificar si el grupo pertenece a la lista de contactos del usuario actual
	    boolean grupoExiste = usuarioActual.getContactos().stream()
	            .anyMatch(contacto -> contacto instanceof Grupo && contacto.equals(grupo));

	    if (!grupoExiste) {
	        throw new ChatControllerException("El grupo no pertenece a la lista de contactos del usuario.");
	    }

	    // Verificar si el contacto existe en la lista de contactos del usuario actual
	    boolean contactoExiste = usuarioActual.getContactos().stream()
	            .filter(c -> c instanceof ContactoIndividual)
	            .map(c -> (ContactoIndividual) c)
	            .anyMatch(c -> c.equals(contactoIndividual));

	    if (!contactoExiste) {
	        throw new ChatControllerException("El contacto no pertenece a la lista de contactos del usuario.");
	    }

	    // Verificar que el contacto no esté ya en el grupo
	    if (grupo.getMiembros().contains(contactoIndividual)) {
	        throw new ChatControllerException("El contacto ya está en el grupo.");
	    }

	    // Agregar el contacto al grupo
	    grupo.addMiembro(contactoIndividual);
	}
	
	/**
	 * Elimina un contacto individual de un grupo existente.
	 *
	 * Se realizan las siguientes validaciones:
	 * - El grupo y el contacto no pueden ser nulos.
	 * - El grupo debe existir en la lista de contactos del usuario.
	 * - El contacto debe estar en el grupo antes de poder eliminarlo.
	 *
	 * @param grupo Grupo del que se eliminará el contacto.
	 * @param contactoIndividual Contacto individual a eliminar del grupo.
	 * @throws ChatControllerException Si el grupo no pertenece al usuario o el contacto no está en el grupo.
	 */
	public void eliminarContactoDeGrupo(Grupo grupo, ContactoIndividual contactoIndividual) throws ChatControllerException {
	    if (grupo == null) {
	        throw new IllegalArgumentException("El grupo no puede ser nulo.");
	    }
	    if (contactoIndividual == null) {
	        throw new IllegalArgumentException("El contacto no puede ser nulo.");
	    }

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
	}

	/**
	 * Actualiza el nombre y/o la imagen de un grupo en la lista de contactos del usuario actual.
	 * 
	 * Se realizan las siguientes validaciones:
	 * - El grupo no puede ser nulo.
	 * - El grupo debe existir en la lista de contactos del usuario.
	 * - Si el nuevo nombre es proporcionado, se actualiza.
	 * - Si la nueva imagen es proporcionada, se actualiza.
	 * - Si ambos valores son nulos, no se realiza ninguna actualización.
	 *
	 * @param grupo Grupo a actualizar.
	 * @param nombre Nuevo nombre del grupo (puede ser nulo si no se desea actualizar).
	 * @param imagen Nueva imagen del grupo (puede ser nula si no se desea actualizar).
	 * @throws ChatControllerException Si el grupo no pertenece a la lista de contactos del usuario.
	 */
	public void actualizarGrupo(Grupo grupo, String nombre, String imagen) throws ChatControllerException {
	    if (grupo == null) {
	        throw new IllegalArgumentException("El grupo no puede ser nulo.");
	    }else if (nombre == null && imagen == null) {
	        throw new IllegalArgumentException("Debe proporcionar al menos un campo para actualizar.");
	    }else if (!usuarioActual.getContactos().contains(grupo)) {
	        throw new ChatControllerException("El grupo no pertenece a la lista de contactos del usuario.");
	    }

	    if (nombre != null) {
	        grupo.setNombre(nombre);
	    }

	    if (imagen != null) {
	        grupo.setImagen(imagen);
	    }
	}

	
	/**
	 * Envía un mensaje a un contacto, ya sea un contacto individual o un grupo.
	 *
	 * Se realizan las siguientes validaciones:
	 * - El contacto debe existir en la lista de contactos del usuario actual.
	 *
	 * El mensaje se guarda en la lista de mensajes del contacto.
	 *
	 * @param contacto Destinatario del mensaje (puede ser ContactoIndividual o Grupo).
	 * @param contenido Contenido del mensaje.
	 * @throws ChatControllerException Si el contacto no está en la lista del usuario actual.
	 */
	public void enviarMensaje(Contacto contacto, String contenido) throws ChatControllerException {
	    if (contacto == null) {
	        throw new IllegalArgumentException("El contacto no puede ser nulo.");
	    }
	    if (contenido == null || contenido.trim().isEmpty()) {
	        throw new IllegalArgumentException("El contenido del mensaje no puede estar vacío.");
	    }
	    
	    Contacto contactoRecuperado = usuarioActual.getContactos()
	    		.stream().filter(c-> c.equals(contacto)).findFirst().orElse(null);
	    	
	    if (contactoRecuperado == null) {
	        throw new ChatControllerException("El contacto no está en la lista del usuario actual.");
	    }

    	Mensaje mensaje = new Mensaje(0, usuarioActual, contenido, LocalDateTime.now(), true);
	    if(contactoRecuperado instanceof ContactoIndividual) {
	    	contactoRecuperado.addMensaje(mensaje);	
	    }else if(contactoRecuperado instanceof Grupo) {
	    	contactoRecuperado.addMensaje(mensaje);
	    	Grupo grupo = (Grupo) contactoRecuperado;
	    	for (ContactoIndividual contactoInd : grupo.getMiembros()) {
	    		contactoInd.addMensaje(mensaje);
			}
	    }
	    
	}

	/**
	 * Busca mensajes en la lista de contactos del usuario aplicando filtros opcionales por
	 * fragmento de texto, nombre del contacto o número de teléfono.
	 * 
	 * Se realizan las siguientes validaciones:
	 * - Se pueden combinar múltiples criterios de búsqueda.
	 * - Se buscan mensajes en la lista de contactos del usuario, ya sean enviados o recibidos.
	 * - Los resultados se ordenan por fecha y hora de envío de manera descendente.
	 * 
	 * @param texto Fragmento de texto a buscar en los mensajes (puede ser nulo).
	 * @param contacto Nombre del contacto a filtrar (puede ser nulo).
	 * @param telefono Número de teléfono del contacto a filtrar (puede ser nulo).
	 * @return Lista de mensajes que coincidan con los criterios de búsqueda.
	 */
	public List<Mensaje> buscarMensajes(String texto, String contacto, String telefono) {
	    return usuarioActual.getContactos().stream()
	            .flatMap(contactoObj -> contactoObj.getMensajes().stream()) // Extrae todos los mensajes de los contactos
	            .filter(mensaje -> 
	                (texto == null || mensaje.getContenido().contains(texto)) && // Filtrar por texto si se proporciona
	                (contacto == null || mensaje.getEmisor().getNombre().equalsIgnoreCase(contacto)) && // Filtrar por nombre si se proporciona
	                (telefono == null || mensaje.getEmisor().getTelefono().equals(telefono)) // Filtrar por teléfono si se proporciona
	            )
	            .sorted(Comparator.comparing(Mensaje::getFechaEnvio).reversed()) // Ordenar por fecha descendente
	            .collect(Collectors.toList());
	}

	
	public void exportarMensajesPDF(Usuario usuario,List<Mensaje> mensajes) {
        // Código para exportar mensajes a PDF
    }

	public void cerrarSesion() {
		this.usuarioActual = null;
	}
}
