package dominio.modelo;

public class Contacto {

	private String nombreContacto;
	private String imagenContacto;
	private int id;

	public Contacto(int id, String nombreContacto, String imagenContacto) {
		this.id = id;
		this.imagenContacto = imagenContacto;
		this.nombreContacto = nombreContacto;
	}

	public Contacto() {
	}

	public int getId() {
		return id;
	}

	public String getNombreContacto() {
		return nombreContacto;
	}

	public String getImagenContacto() {
		return imagenContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public void setImagenContacto(String imagenContacto) {
		this.imagenContacto = imagenContacto;
	}

	public void setId(int id) {
		this.id = id;
	}

}
