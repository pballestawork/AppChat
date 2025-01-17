package dominio.model;

public class Usuario {

	private int id;
	private String nombre;
	private String telefono;
	private String email;
	private String contrasena;
	private String fotoPerfil;
	private boolean esPremium;

	public Usuario(int id, String nombre, String telefono, String email, String contrasena, String fotoPerfil,
			boolean esPremium) {
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.contrasena = contrasena;
		this.fotoPerfil = fotoPerfil;
		this.esPremium = esPremium;
	}

	public Usuario() {
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getEmail() {
		return email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public boolean isEsPremium() {
		return esPremium;
	}

	public void convertirAPremium() {
		this.esPremium = true;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public void setEsPremium(boolean esPremium) {
		this.esPremium = esPremium;
	}

	public void actualizarPerfil(String nombre, String email, String fotoPerfil) {
		this.nombre = nombre;
		this.email = email;
		this.fotoPerfil = fotoPerfil;
	}

}
