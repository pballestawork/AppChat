package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import dominio.controlador.ChatControllerException;
import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Descuento;
import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;

public class ChatControllerStub {	
	private static ChatControllerStub unicaInstancia;
	
	private Usuario usuarioActual;
	private Map<String,Usuario> usuarios;
	private List<ContactoIndividual> usuariosGrupo;
	private AtomicInteger contadorGrupoId;
	
	
	private ChatControllerStub () {
		usuarioActual = null; 
		contadorGrupoId = new AtomicInteger(1); // Inicializar contador de IDs para grupos
		
		// Inicialización de usuarios
		Usuario usuario = new Usuario(1,"Pablo","1","pablo@gmail.com","123","/FotosPerfil/Perfil_2.png",false,"Hola",LocalDate.now(),new LinkedList<Contacto>());
		Usuario usuario2 = new Usuario(2,"Alvaro","2","alvaro@gmail.com","123","/FotosPerfil/Perfil_1.png",false,"Hola",LocalDate.now(),new LinkedList<Contacto>());
		Usuario usuario3 = new Usuario(3,"Laura","3","laura@gmail.com","123","/FotosPerfil/Perfil_3.png",false,"Hola",LocalDate.now(),new LinkedList<Contacto>());
		Usuario usuario4 = new Usuario(3,"Pepe","4","pepe@gmail.com","123","/FotosPerfil/Perfil_2.png",false,"Hola,soy pepe",LocalDate.now(),new LinkedList<Contacto>());
		
		usuarios = new HashMap<String, Usuario>();
		usuarios.put(usuario.getTelefono(),usuario);
		usuarios.put(usuario2.getTelefono(),usuario2);
		usuarios.put(usuario3.getTelefono(),usuario3);
		usuarios.put(usuario4.getTelefono(),usuario4);
		
		// Crear contactos
		ContactoIndividual alvaroDePablo = FactoriaPruebas.crearContactoIndividual(usuario2);
		ContactoIndividual lauraDePablo = FactoriaPruebas.crearContactoIndividual(usuario3);
		ContactoIndividual pepeDePablo = FactoriaPruebas.crearContactoIndividual(usuario4);
		pepeDePablo.setNombre("");
		
		ContactoIndividual pabloDeAlvaro = FactoriaPruebas.crearContactoIndividual(usuario);
		ContactoIndividual lauraDeAlvaro = FactoriaPruebas.crearContactoIndividual(usuario3);
		
		ContactoIndividual pabloDeLaura = FactoriaPruebas.crearContactoIndividual(usuario);
		ContactoIndividual alvaroDeLaura = FactoriaPruebas.crearContactoIndividual(usuario2);
		
		// Asignar contactos a usuarios
		usuario.addContacto(alvaroDePablo);
		usuario.addContacto(lauraDePablo);
		usuario.addContacto(pepeDePablo);
		
		usuario2.addContacto(pabloDeAlvaro);
		usuario2.addContacto(lauraDeAlvaro);
		
		usuario3.addContacto(pabloDeLaura);
		usuario3.addContacto(alvaroDeLaura);
	
		// Añadir mensajes iniciales con sus respectivos receptores
		Mensaje m1 = new Mensaje(1, usuario, alvaroDePablo, "Hola", LocalDateTime.now(), true);
		Mensaje m2 = new Mensaje(2, usuario, alvaroDePablo, "Como estas Alvaro?", LocalDateTime.now(), true);
		Mensaje m3 = new Mensaje(3, usuario2, pabloDeAlvaro, "Hola", LocalDateTime.now(), false);
		Mensaje m4 = new Mensaje(4, usuario2, pabloDeAlvaro, "Bien y tu?", LocalDateTime.now(), false);
		Mensaje m5 = new Mensaje(5, usuario, alvaroDePablo, "Bien gracias", LocalDateTime.now(), true);
		
		alvaroDePablo.addMensaje(m1);
		alvaroDePablo.addMensaje(m2);
		alvaroDePablo.addMensaje(m3);
		alvaroDePablo.addMensaje(m4);
		alvaroDePablo.addMensaje(m5);
		
		// Mensajes para Alvaro (enviados por Pablo, recibidos por Alvaro)
		Mensaje m6 = new Mensaje(1, usuario, pabloDeAlvaro, "Hola", LocalDateTime.now(), false);
		Mensaje m7 = new Mensaje(2, usuario, pabloDeAlvaro, "Como estas Alvaro?", LocalDateTime.now(), false);
		Mensaje m8 = new Mensaje(3, usuario2, alvaroDePablo, "Hola", LocalDateTime.now(), true);
		Mensaje m9 = new Mensaje(4, usuario2, alvaroDePablo, "Bien y tu?", LocalDateTime.now(), true);
		Mensaje m10 = new Mensaje(5, usuario, pabloDeAlvaro, "Bien gracias", LocalDateTime.now(), false);
		
		pabloDeAlvaro.addMensaje(m6);
		pabloDeAlvaro.addMensaje(m7);
		pabloDeAlvaro.addMensaje(m8);
		pabloDeAlvaro.addMensaje(m9);
		pabloDeAlvaro.addMensaje(m10);
		
		// Mensajes de Pablo a Laura
		Mensaje m11 = new Mensaje(3, usuario, lauraDePablo, "Hola", LocalDateTime.now(), true);
		Mensaje m12 = new Mensaje(4, usuario, lauraDePablo, "Como estas Laura?", LocalDateTime.now(), true);
		
		lauraDePablo.addMensaje(m11);
		lauraDePablo.addMensaje(m12);
		
		// Mensajes para Laura (recibidos de Pablo)
		Mensaje m13 = new Mensaje(3, usuario, pabloDeLaura, "Hola", LocalDateTime.now(), false);
		Mensaje m14 = new Mensaje(4, usuario, pabloDeLaura, "Como estas Laura?", LocalDateTime.now(), false);
		
		pabloDeLaura.addMensaje(m13);
		pabloDeLaura.addMensaje(m14);
		
		// Crear grupo
		usuariosGrupo = new LinkedList<ContactoIndividual>();
		usuariosGrupo.add(alvaroDePablo);
		usuariosGrupo.add(lauraDePablo);
		
		// Ahora usamos el método simplificado que genera un ID único automáticamente
		usuario.crearGrupo("Grupo", usuariosGrupo, "/FotosPerfil/Perfil_2.png");
	}
	
	public static ChatControllerStub getUnicaInstancia() {
		if(unicaInstancia == null)
			unicaInstancia = new ChatControllerStub();
		return unicaInstancia;
	}
	
	
	public void registrarUsuario(String nombre, LocalDate fechaNacimiento, String email, String fotoPerfil,
			String telefono, String contrasena,String saludo) throws ChatControllerException {
		
		if(usuarios.containsKey(telefono)) {
			throw new ChatControllerException("El telefono "+ telefono +" ya está registrado.");
		}
		
		Usuario u = new Usuario(0, nombre, telefono, email,contrasena, fotoPerfil,false ,saludo, fechaNacimiento);
		
		usuarios.put(telefono, u);
	}
	
	
	public Usuario iniciarSesion(String telefono, String contrasena) throws ChatControllerException {	
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
	
	public void actualizarPerfil(String nuevoSaludo, String nuevaImagen) {
	    
	    if (nuevoSaludo != null) {
	        usuarioActual.setSaludo(nuevoSaludo);
	    }

	    if (nuevaImagen != null) {
	        usuarioActual.setFotoPerfil(nuevaImagen);
	    }
	}

	public void agregarContacto(String nombre, String telefono) throws ChatControllerException {
		
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
	

	public void eliminarContacto(Contacto contacto) throws ChatControllerException {
	    // Verificar si el contacto está en la lista del usuario actual
	    if (!usuarioActual.getContactos().contains(contacto)) {
	        throw new ChatControllerException("El contacto o grupo no está en la lista de contactos del usuario.");
	    }

	    // Eliminar el contacto de la lista
	    usuarioActual.deleteContacto(contacto);
	}
	
	
	public void crearGrupo(List<ContactoIndividual> miembros, String nombreGrupo, String imagenGrupo) throws ChatControllerException {

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
		
		 // Utilizar el método de Usuario que ahora genera IDs automáticamente
		usuarioActual.crearGrupo(nombreGrupo, miembros, imagenGrupo);
	}

	
	public void agregarContactoAGrupo(Grupo grupo, ContactoIndividual contactoIndividual) throws ChatControllerException {

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
	

	public void eliminarContactoDeGrupo(Grupo grupo, ContactoIndividual contactoIndividual) throws ChatControllerException {

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

	public void actualizarGrupo(Grupo grupo, String nombre, String imagen) throws ChatControllerException {
	    if (nombre != null) {
	        grupo.setNombre(nombre);
	    }

	    if (imagen != null) {
	        grupo.setImagen(imagen);
	    }
	}

	public void enviarMensaje(Contacto contacto, String contenido) throws ChatControllerException {
	    
	    // PROBLEMA: Múltiples contactos tienen el mismo ID (0), lo que causa confusión
	    // en lugar de buscar solo por ID, buscaremos también por nombre para diferenciarlos
	    Contacto contactoRecuperado = usuarioActual.getContactos()
	            .stream()
	            .filter(c -> c.getId() == contacto.getId() && c.getNombre().equals(contacto.getNombre()))
	            .findFirst()
	            .orElse(null);
	    
	    // Si no se encuentra, intentar buscar solo por nombre como respaldo
	    if (contactoRecuperado == null) {
	        contactoRecuperado = usuarioActual.getContactos()
	            .stream()
	            .filter(c -> c.getNombre().equals(contacto.getNombre()))
	            .findFirst()
	            .orElse(null);
	    }
	    
	    // Finalmente, si aún no encontramos nada, intentar por equals
	    if (contactoRecuperado == null) {
	        contactoRecuperado = usuarioActual.getContactos()
	            .stream().filter(c -> c.equals(contacto)).findFirst().orElse(null);
	    }
	    
	    if (contactoRecuperado == null) {
	        throw new ChatControllerException("El contacto no está en la lista del usuario actual.");
	    }

	    // Crear mensaje con el receptor explícito
	    Mensaje mensaje = new Mensaje(0, usuarioActual, contactoRecuperado, contenido, LocalDateTime.now(), true);
	    
	    if (contactoRecuperado instanceof ContactoIndividual) {
	        // Enviar mensaje a contacto individual
	        contactoRecuperado.addMensaje(mensaje);
	        
	        // Cuando es un contacto individual, crear una copia del mensaje como recibido para el destinatario
	        ContactoIndividual contactoInd = (ContactoIndividual) contactoRecuperado;
	        Usuario usuarioDestinatario = contactoInd.getUsuario();
	        
	        // Buscar el contacto del usuario actual en la lista del destinatario
	        ContactoIndividual miContactoEnDestinatario = null;
	        for (Contacto c : usuarioDestinatario.getContactos()) {
	            if (c instanceof ContactoIndividual) {
	                ContactoIndividual ci = (ContactoIndividual) c;
	                if (ci.getUsuario().equals(usuarioActual)) {
	                    miContactoEnDestinatario = ci;
	                    break;
	                }
	            }
	        }
	        
	        // Si el destinatario tiene al emisor como contacto, añadir el mensaje como recibido
	        if (miContactoEnDestinatario != null) {
	            Mensaje mensajeRecibido = mensaje.clone();
	            mensajeRecibido.setTipo(false); // false = mensaje recibido
	            miContactoEnDestinatario.addMensaje(mensajeRecibido);
	        }
	    } else if (contactoRecuperado instanceof Grupo) {
	        // Enviar mensaje a grupo
	        Grupo grupo = (Grupo) contactoRecuperado;
	        
	        // Añadir el mensaje al grupo
	        contactoRecuperado.addMensaje(mensaje);
	        
	        // Comprobar si es un emoji
	        if (contenido.startsWith("EMOJI:")) {
	            // Si es un emoji, enviarlo tal cual a los miembros del grupo
	            // para que se procese correctamente como emoji
	            
	            // Enviar el emoji a cada miembro del grupo
	            for (ContactoIndividual contactoInd : grupo.getMiembros()) {
	                // Crear una copia del mensaje con el receptor correcto
	                // Importante: El tipo debe seguir siendo true ya que el usuario actual es el emisor
	                Mensaje mensajeEmoji = new Mensaje(0, usuarioActual, contactoInd, contenido, LocalDateTime.now(), true);
	                contactoInd.addMensaje(mensajeEmoji);
	            }
	        } else {
	            // Para mensajes normales, enviar el contenido original sin prefijo
	            
	            // Enviar el mensaje a cada miembro del grupo
	            for (ContactoIndividual contactoInd : grupo.getMiembros()) {
	                // Crear una copia del mensaje con el receptor correcto
	                // Importante: El tipo debe seguir siendo true ya que el usuario actual es el emisor
	                Mensaje mensajeNormal = new Mensaje(0, usuarioActual, contactoInd, contenido, LocalDateTime.now(), true);
	                contactoInd.addMensaje(mensajeNormal);
	            }
	        }
	    }
	}


	public List<Mensaje> buscarMensajes(String texto, String contacto, String telefono) {
	    return usuarioActual.getContactos().stream()
	            .flatMap(contactoObj -> contactoObj.getMensajes().stream()) // Extrae todos los mensajes de los contactos
	            .filter(mensaje -> 
	                (texto == null || mensaje.getContenido().contains(texto)) && // Filtrar por texto si se proporciona
	                (contacto == null || 
	                    (mensaje.getEmisor().getNombre().equalsIgnoreCase(contacto) || 
	                     (mensaje.getReceptor() != null && 
	                      ((mensaje.getReceptor() instanceof ContactoIndividual && 
	                        ((ContactoIndividual)mensaje.getReceptor()).getUsuario().getNombre().equalsIgnoreCase(contacto)) || 
	                       mensaje.getReceptor().getNombre().equalsIgnoreCase(contacto))))) && 
	                (telefono == null || 
	                    mensaje.getEmisor().getTelefono().equals(telefono) || 
	                    (mensaje.getReceptor() instanceof ContactoIndividual && 
	                     ((ContactoIndividual)mensaje.getReceptor()).getUsuario().getTelefono().equals(telefono)))
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
	
	/**
	 * Actualiza el estado premium del usuario actual.
	 * En una implementación real, este método también realizaría la integración con un sistema de pagos.
	 * 
	 * @return true si la actualización fue exitosa, false en caso contrario
	 */
	public boolean actualizarUsuarioAPremium() {
		if (usuarioActual == null) {
			return false;
		}
		
		// Si el usuario ya es premium, no hacemos nada pero indicamos éxito
		if (usuarioActual.isEsPremium()) {
			return true;
		}
		
		try {
			// Actualizar el estado premium del usuario
			usuarioActual.convertirAPremium();
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
		return PDFGenerator.generarInformeContactos(usuarioActual, rutaDestino);
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
