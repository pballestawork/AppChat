package dominio.modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import persistencia.dao.Identificable;

public class Usuario implements Identificable {

	private int id;
	private String nombre;
	private String telefono;
	private String email;
	private String contrasena;
	private String fotoPerfil;
	private String saludo;
	private boolean esPremium;
	private List<Contacto> contactos = new LinkedList<Contacto>();

	public Usuario(int id, String nombre, String telefono, String email, String contrasena, String fotoPerfil,
			boolean esPremium, String saludo, List<Contacto> contactos) {
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.contrasena = contrasena;
		this.fotoPerfil = fotoPerfil;
		this.esPremium = esPremium;
		this.contactos = contactos;
		this.saludo = saludo;
	}

	public Usuario() {
	}

	@Override
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

	public String getSaludo() {
		return saludo;
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

	public List<Contacto> getContactos() {
		return contactos;
	}

	public void setSaludo(String saludo) {
		this.saludo = saludo;
	}

	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}

	public void actualizarPerfil(String nombre, String email, String fotoPerfil) {
		this.nombre = nombre;
		this.email = email;
		this.fotoPerfil = fotoPerfil;
	}

	@Override
	public String toString() {
		String toString = "Usuario {" + "\n\tid : " + id + ",\n\tnombre : " + nombre + ",\n\ttelefono : " + telefono
				+ ",\n\temail : " + email + ",\n\tcontrasena : " + contrasena + ",\n\tfotoPerfil : " + fotoPerfil
				+ ",\n\tesPremium : " + esPremium + ",\n\tcontactos : {";
		for (Contacto contacto : contactos) {
			toString += "\n\t\tnombre : " + contacto.getNombre();
		}
		toString += contactos.size() > 0 ? "}" : "\n\t}";
		return toString += "\n}";
	}

	@Override
	public int hashCode() {
		return Objects.hash(contactos, contrasena, email, esPremium, fotoPerfil, id, nombre, telefono);
	}

	/**
	 * Se considera que dos usuarios son iguales si sus telefonos son iguales
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;

		// DONE Cambiar el return comparando valor de los atributos
		return telefono == other.getTelefono();
	}

}
