package dominio.controlador;

import java.util.List;

import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;

public class ChatController {
	
	public Usuario iniciarSesion(String email, String contrasena) {
		// Código para iniciar sesión
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
