package dominio.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import persistencia.dao.Identificable;

public class Mensaje implements Identificable{

	private int id;
	private Usuario emisor;
	private String contenido;
	private LocalDateTime fechaEnvio;
	private Boolean tipo;
	
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	public Mensaje(int id, Usuario emisor, String contenido, LocalDateTime fechaEnvio, Boolean tipo) {
		this.id = id;
		this.emisor = emisor;
		this.tipo = tipo;
		this.contenido = contenido;
		this.fechaEnvio = fechaEnvio == null ? null : fechaEnvio.withNano(0);//Fechas ajustadas al formato
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
	
	
	
	/*
	 * Getters and setters
	 */
	
	@Override
	public String toString() {
		return "Mensaje [emisor=" + emisor.getTelefono() + ", contenido=" + contenido + ", fechaEnvio=" + fechaEnvio.format(FORMATTER) + "]";
	}

	@Override
	public int getId() {
		return id;
	}

	public Usuario getEmisor() {
		return emisor;
	}

	public Boolean getTipo() {
		return tipo;
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

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public void setFechaEnvio(LocalDateTime fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public void setTipo(Boolean tipo) {
		this.tipo = tipo;
	}
}
