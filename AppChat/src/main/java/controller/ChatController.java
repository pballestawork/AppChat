package controller;

import java.util.List;

import model.Grupo;
import model.Mensaje;
import model.Usuario;

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
