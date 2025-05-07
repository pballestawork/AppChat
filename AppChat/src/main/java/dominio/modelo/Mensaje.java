package dominio.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import persistencia.dao.Identificable;

public class Mensaje implements Identificable, Cloneable {

	private int id;
	private Usuario emisor;
	private Contacto receptor; // Puede ser ContactoIndividual o Grupo
	private String contenido;
	private LocalDateTime fechaEnvio;
	
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	public Mensaje(int id, Usuario emisor, Contacto receptor, String contenido, LocalDateTime fechaEnvio) {
		this.id = id;
		this.emisor = emisor;
		this.receptor = receptor;
		this.contenido = contenido;
		// Ajustar la fecha al formato sin nanosegundos para consistencia
		this.fechaEnvio = fechaEnvio == null ? LocalDateTime.now().withNano(0) : fechaEnvio.withNano(0);
	}

	public Mensaje() {
	}
	
	/**
	 * Dos mensajes son iguales si comparten Id
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mensaje other = (Mensaje) obj;
		return id == other.getId();
	}
	
	@Override
	public Mensaje clone() {
	    Mensaje clon = new Mensaje();
	    clon.id = this.id;
	    clon.emisor = this.emisor;
	    clon.receptor = this.receptor;
	    clon.contenido = this.contenido;
	    clon.fechaEnvio = this.fechaEnvio;
	    return clon;
	}
	
	@Override
	public String toString() {
		return "Mensaje [emisor=" + emisor.getTelefono() + ", contenido=" + contenido + ", fechaEnvio=" + fechaEnvio.format(FORMATTER) + "]";
	}
	
	
	/*
	 * Getters and setters
	 */
	
	@Override
	public int getId() {
		return id;
	}

	public Usuario getEmisor() {
		return emisor;
	}
	
	/**
	 * Obtiene el contacto destinatario del mensaje
	 * @return El contacto receptor (puede ser ContactoIndividual o Grupo)
	 */
	public Contacto getReceptor() {
		return receptor;
	}

	public String getContenido() {
		return contenido;
	}

	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}
	
	public void setReceptor(Contacto receptor) {
		this.receptor = receptor;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public void setFechaEnvio(LocalDateTime fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	
	/**
	 * Comprueba si el mensaje contiene el texto especificado (ignorando mayúsculas/minúsculas)
	 * @param texto El texto a buscar
	 * @return true si el mensaje contiene el texto, false en caso contrario
	 */
	public boolean contienePalabra(String texto) {
		return texto != null && contenido != null && 
		       contenido.toLowerCase().contains(texto.toLowerCase());
	}

	public boolean isEnviado(Usuario usuario) {
		return usuario.equals(emisor);
	}
}
