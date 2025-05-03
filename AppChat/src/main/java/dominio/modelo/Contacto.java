package dominio.modelo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import persistencia.dao.Identificable;

public class Contacto implements Identificable {

	private int id;
	private String nombre;
	private List<Mensaje> mensajes;

	public Contacto(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
		this.mensajes = new LinkedList<Mensaje>();
	}

	public Contacto() {
	}
	
	/**
	 * Dos contactos son iguales si comparten mismo id
	 * NOTA: Varios contactos podrían hacer referencia 
	 * al mismo usuario pero con distinto nombre
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contacto other = (Contacto) obj;
		return id == other.id;
	}
	
	
	
	/*
	 * Getters and setters
	 */
	
	@Override
	public String toString() {
		return "Contacto [id=" + id + ", nombre=" + nombre + "]";
	}

	@Override
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
	
	public Mensaje addMensaje(Usuario emisor, String contenido) {
		Mensaje m = new Mensaje(0,emisor,this,contenido, LocalDateTime.now(),true);
		this.addMensaje(m);
		return m;
	}
	
	public void deleteMensaje(Mensaje mensaje) {
		this.mensajes.remove(mensaje);
	}	
}
