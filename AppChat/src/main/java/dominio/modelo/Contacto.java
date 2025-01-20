package dominio.modelo;

import java.util.List;

public class Contacto {

	private int id;
	private String nombre;
	private List<Mensaje> mensajes;

	public Contacto(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public Contacto() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}	
	
	public void addMensaje(Mensaje mensaje) {
		this.mensajes.add(mensaje);
	}
	
	public void deleteMensaje(Mensaje mensaje) {
		this.mensajes.remove(mensaje);
	}

	
}
