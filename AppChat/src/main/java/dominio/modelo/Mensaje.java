package dominio.modelo;

import java.time.LocalDateTime;

public class Mensaje {

	private int id;
	private Usuario emisor;
	private String contenido;
	private LocalDateTime fechaEnvio;
	private Boolean tipo;

	public Mensaje(int id, Usuario emisor, Boolean tipo, String contenido, LocalDateTime fechaEnvio) {
		this.id = id;
		this.emisor = emisor;
		this.tipo = tipo;
		this.contenido = contenido;
		this.fechaEnvio = fechaEnvio;
	}

	public Mensaje() {
	}

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
