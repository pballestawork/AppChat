package appChatStub.controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
	private Map<String,Usuario> usuarios;
	private Usuario usuarioActual;
	
	
	public ChatControllerStub () {
		usuarioActual = null; 
		
		Usuario usuario = new Usuario(0,"Pablo","600600600","pablo@gmail.com","1234","",false,"Hola",null);
		usuario.setContactos(new LinkedList<Contacto>());
		
		usuarios = new HashMap<String, Usuario>();
		usuarios.put(usuario.getTelefono(),usuario);
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
	public Usuario registrarUsuario(String nombre, LocalDateTime fechaNacimiento, String email, String fotoPerfil,
			String telefono, String contrasena, String contrasenaRepetida) throws ChatControllerException {
		if (nombre == null || nombre.isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacio.");
		} else if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("El email no puede ser nulo o vacio.");
		} else if (telefono == null || telefono.isEmpty()) {
			throw new IllegalArgumentException("El telefono no puede ser nulo o vacio.");
		} else if (contrasena == null || contrasena.isEmpty()) {
			throw new IllegalArgumentException("La contraseña no puede ser nulo o vacio.");
		} else if (contrasenaRepetida == null || !contrasenaRepetida.equals(contrasena)) {
			throw new IllegalArgumentException("Las contraseñas no coinciden");
		} else if(fechaNacimiento != null && fechaNacimiento.isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException("La fecha de nacimiento no puede ser posterior a la fecha actual.");
		}
		
		if(usuarios.containsKey(telefono)) {
			throw new ChatControllerException("El telefono "+telefono+" ya está registrado.");
		}
		
		Usuario u = new Usuario(0, nombre, telefono, email,contrasena, fotoPerfil,false,null,new LinkedList<Contacto>());
		return u;
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
		if (telefono == null || telefono.isEmpty()) {
			throw new IllegalArgumentException("El telefono no puede ser nulo o vacio.");
		} else if (contrasena == null || contrasena.isEmpty()) {
			throw new IllegalArgumentException("La contraseña no puede ser nula o vacia.");
		}
		
		Usuario u = usuarios.get(telefono);
		
		if(u == null || !u.getContrasena().equals(contrasena)) {
			throw new ChatControllerException("El telefono o la contraseña son incorrectos.");
		}
		
		usuarioActual = u;
		return u;
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
	public void anadirContacto(String nombre, String telefono) throws ChatControllerException {
		if (nombre == null || nombre.isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacio.");
		} else if (telefono == null || telefono.isEmpty()) {
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
			throw new ChatControllerException("El numero no existe.");
		}
		
		
		ContactoIndividual c = new ContactoIndividual(0, nombre, usuarios.get(telefono));
		usuarioActual.addContacto(c);
	}
	
	/**
	 * Elimina un contacto existente de la lista de contactos.
	 * 
	 * Esta función permite a un usuario registrado eliminar un contacto proporcionando
	 * su numero de telefono.
	 * 
	 * Se realizan las siguientes validaciones:
	 * - Verifica que el número de teléfono exista en la lista de contactos.
	 * 
	 * Si el contacto no existe se muestra un mensaje de error.
	 * Si la eliminación es exitosa, se actualiza la lista de contactos y se muestra.
	 * 
	 * @param nombre
	 * @param numero
	 * @throws ChatControllerException 
	 */
	public void eliminarContacto(String numero) throws ChatControllerException {
		if(numero == null || numero.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		Contacto c = usuarioActual.getContactos().stream()
	            .filter(contacto -> contacto instanceof ContactoIndividual) 
	            .map(contacto -> (ContactoIndividual) contacto) 
	            .filter(contacto -> contacto.getUsuario().getTelefono().equals(numero)) 
	            .findFirst()
	            .orElse(null);

		if(c == null) {
			throw new ChatControllerException("No tienes ningún contacto con el número "+ numero+".");
		}
		
		usuarioActual.deleteContacto(c);
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
	public void crearGrupo(List<ContactoIndividual> miembros ,String nombreGrupo, String imagenGrupo) throws ChatControllerException {
		if(nombreGrupo == null || nombreGrupo.isEmpty()) {
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
	 * Elimina un grupo de contactos de la lista de contactos del usuario.
	 *
	 * Se realizan las siguientes validaciones:
	 * - El grupo no puede ser nulo.
	 * - El grupo debe existir en la lista de contactos del usuario.
	 *
	 * Si la eliminación es exitosa, el grupo se elimina de la lista de contactos del usuario.
	 *
	 * @param grupo Grupo a eliminar.
	 * @throws ChatControllerException Si el grupo no existe en la lista de contactos del usuario.
	 */
	public void eliminarGrupo(Grupo grupo) throws ChatControllerException {
	    if (grupo == null) {
	        throw new IllegalArgumentException("El grupo no puede ser nulo.");
	    }

	    // Verificar si el grupo está en la lista de contactos del usuario actual
	    boolean eliminado = usuarioActual.getContactos().removeIf(contacto -> 
	            contacto instanceof Grupo && contacto.equals(grupo));

	    if (!eliminado) {
	        throw new ChatControllerException("El grupo no existe en la lista de contactos del usuario.");
	    }
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
	    if (contenido == null || contenido.isEmpty()) {
	        throw new IllegalArgumentException("El contenido del mensaje no puede estar vacío.");
	    }

	    // Verificar si el contacto está en la lista de contactos del usuario actual
	    boolean contactoExiste = usuarioActual.getContactos().stream()
	            .anyMatch(c -> c.equals(contacto));

	    if (!contactoExiste) {
	        throw new ChatControllerException("El contacto no está en la lista del usuario actual.");
	    }

	    Mensaje mensaje = new Mensaje(0, usuarioActual, contacto, contenido, LocalDateTime.now(), true);

	    // Agregar el mensaje a la lista de mensajes del contacto
	    contacto.addMensaje(mensaje);
	}

	public List<Mensaje> buscarMensajes(String filtro) {
		// Código para buscar mensajes
		return null;
	}
	
	public void exportarMensajesPDF(Usuario usuario,List<Mensaje> mensajes) {
        // Código para exportar mensajes a PDF
    }
}
