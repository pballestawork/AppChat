package dominio.modelo;

import java.util.Date;

public class Mensaje {

	private int id;
	private Usuario emisor;
	private String contenido;
	private Date fechaEnvio;
	private Boolean tipo;

	public Mensaje(int id, Usuario emisor, Boolean tipo, String contenido, Date fechaEnvio) {
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

	public Date getFechaEnvio() {
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

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public void setTipo(Boolean tipo) {
		this.tipo = tipo;
	}

}
