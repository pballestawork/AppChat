package appChatStub.controlador;

import java.util.LinkedList;
import java.util.List;

import dominio.modelo.Contacto;
import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;

public class ChatControllerStub {
	private Usuario usuario;
	
	public ChatControllerStub () {
		usuario = new Usuario(0,"Pablo","600600600","pablo@gmail.com","1234","",false,"Hola",null);
		usuario.setContactos(new LinkedList<Contacto>());
	}
	
	public Usuario iniciarSesion(String telefono, String contrasena) {
		if(usuario.getTelefono() == telefono && usuario.getContrasena() == contrasena)
			return usuario;
		
		return null;
	}
	
	public void registrarUsuario(Usuario usuario) {
		// Código para registrar un usuario
	}
	
	public void enviarMensaje(Usuario emisor, Usuario receptor, String contenido) {
		// Código para enviar un mensaje
	}
	
	public Grupo crearGrupo(Usuario administrador,List<Usuario>  miembros ,String nombreGrupo) {
		// Código para crear un grupo
		return null;
	}

	public List<Mensaje> buscarMensajes(String filtro) {
		// Código para buscar mensajes
		return null;
	}
	
	public void añadirContacto(String nombre, String numero) {
		// Código para añadir un contacto
	}
	
	public void exportarMensajesPDF(Usuario usuario,List<Mensaje> mensajes) {
        // Código para exportar mensajes a PDF
    }
}
