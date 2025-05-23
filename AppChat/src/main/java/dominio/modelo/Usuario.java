package dominio.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
	private LocalDate fechaNacimiento;
	private List<Contacto> contactos = new LinkedList<Contacto>();
	
		
	public Usuario(int id, String nombre, String telefono, String email, String contrasena, String fotoPerfil,
			boolean esPremium, String saludo, LocalDate fechaNacimiento, List<Contacto> contactos) {
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.contrasena = contrasena;
		this.fotoPerfil = fotoPerfil;
		this.esPremium = esPremium;
		this.fechaNacimiento = fechaNacimiento;
		this.contactos = contactos != null ? contactos : new ArrayList<>();
		this.saludo = saludo;
	}
	
	public Usuario(int id, String nombre, String telefono, String email, String contrasena, String fotoPerfil,
			boolean esPremium, String saludo, LocalDate fechaNacimiento) {
		this(id, nombre, telefono, email, contrasena, fotoPerfil, esPremium, saludo,fechaNacimiento, new ArrayList<>());
	}


	public Usuario() {
	}

	public boolean validarContrasena(String password) {
		return contrasena.equals(password);
	}

	/**
	 * Busca un contacto individual por el número de teléfono del usuario asociado.
	 */
	public ContactoIndividual buscarContactoPorTelefono(String telefono) {
		return contactos.stream()
				.filter(contacto -> contacto instanceof ContactoIndividual)
				.map(contacto -> (ContactoIndividual) contacto)
				.filter(c -> c.perteneceaUsuarioConTelefono(telefono))
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Verifica si existe un contacto con el número de teléfono especificado.
	 */
	public boolean tieneContactoConTelefono(String telefono) {
		return buscarContactoPorTelefono(telefono) != null;
	}
	
	/**
	 * Crea un nuevo contacto individual asociado a un usuario externo y lo añade a la lista de contactos.
	 */
	public ContactoIndividual addContactoIndividual(String nombre, Usuario usuarioContacto) {
		ContactoIndividual contacto = new ContactoIndividual(0, nombre, usuarioContacto);
		this.addContacto(contacto);
		return contacto;
	}
	
	/**
	 * Crea un nuevo grupo con los miembros especificados y lo añade a la lista de contactos.
	 * El grupo recibirá automáticamente un ID único.
	 */
	public Grupo addGrupo(String nombreGrupo, List<ContactoIndividual> miembros, String imagenGrupo) {
		Grupo grupo = new Grupo(0, nombreGrupo, miembros, imagenGrupo);
		this.addContacto(grupo);
		return grupo;
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
				+ ",\n\tsaludo : " + saludo + ",\n\tesPremium : " + esPremium +  ",\n\tcontactos : {";
		for (Contacto contacto : contactos) {
			toString += "\n\t\tnombre : " + contacto.getNombre();
		}
		toString += contactos.size() == 0 ? "}" : "\n\t}";
		return toString += "\n}";
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
		return telefono.equals(other.getTelefono());
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

	public void addContacto(Contacto contactoPedro) {
		this.contactos.add(contactoPedro);
	}
	
	public void deleteContacto(Contacto contactoPedro) {
		this.contactos.remove(contactoPedro);
	}
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	
}

