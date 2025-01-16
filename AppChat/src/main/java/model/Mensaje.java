package model;

import java.util.Date;

public class Mensaje {
	
	private int id;
	private Usuario emisor;
	private Usuario receptor;
	private String contenido;
	private Date fechaEnvio;
	
	public Mensaje(int id, Usuario emisor, Usuario receptor, String contenido, Date fechaEnvio) {
		this.id = id;
		this.emisor = emisor;
		this.receptor = receptor;
		this.contenido = contenido;
		this.fechaEnvio = fechaEnvio;
	}

	public int getId() {
		return id;
	}

	public Usuario getEmisor() {
		return emisor;
	}

	public Usuario getReceptor() {
		return receptor;
	}

	public String getContenido() {
		return contenido;
	}

	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	
	public boolean esDeContacto(Contacto contacto) {
		return emisor.getTelefono().equals(contacto.getTelefonoContacto()) || receptor.getTelefono().equals(contacto.getTelefonoContacto());
    }
	
	public boolean esDeGrupo(Grupo grupo) {
		for (Usuario usuario : grupo.getMiembros()) {
			if (usuario.getTelefono().equals(emisor.getTelefono())
					|| usuario.getTelefono().equals(receptor.getTelefono())) {
				return true;
			}
		}
		return false;
	}

}
