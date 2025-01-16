package model;

public class Usuario {
	
	private int id;
	private String nombre;
	private String telefono;
	private String email;
	private String contrasena;
	private String fotoPerfil;
	private boolean esPremium;
	
	public Usuario(int id, String nombre, String telefono, String email, String contrasena, String fotoPerfil, boolean esPremium) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.contrasena = contrasena;
        this.fotoPerfil = fotoPerfil;
        this.esPremium = esPremium;
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
	
	public void actualizarPerfil(String nombre, String email,String fotoPerfil) {
		this.nombre = nombre;
		this.email = email;
		this.fotoPerfil = fotoPerfil;
	}
	

}
