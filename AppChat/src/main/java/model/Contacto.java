package model;

public class Contacto {
	
	private String nombreContacto;
	private String telefonoContacto;
	
	public Contacto(String nombreContacto, String telefonoContacto) {
		this.nombreContacto = nombreContacto;
		this.telefonoContacto = telefonoContacto;
	}

	public String getNombreContacto() {
		return nombreContacto;
	}

	public String getTelefonoContacto() {
		return telefonoContacto;
	}
	

}
