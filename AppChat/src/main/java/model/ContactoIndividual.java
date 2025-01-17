package model;

public class ContactoIndividual extends Contacto {

	private String telefonoContacto;

	public ContactoIndividual(int id, String nombreContacto, String telefonoContacto, String imagenContacto) {
		super(id, nombreContacto, imagenContacto);
		this.telefonoContacto = telefonoContacto;
	}

	public ContactoIndividual() {
		super();
	}

	public String getTelefonoContacto() {
		return telefonoContacto;
	}

	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}

}
