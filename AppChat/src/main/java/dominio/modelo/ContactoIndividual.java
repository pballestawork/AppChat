package dominio.modelo;

public class ContactoIndividual extends Contacto {

	private Usuario usuario;

	public ContactoIndividual(int id, String nombre, Usuario usuario) {
		super(id, nombre);
		this.usuario = usuario;
	}

	public ContactoIndividual() {
		super();
	}
	
	@Override
	public String toString() {
		return "ContactoIndividual [nombre= "+ super.getNombre() + ", usuario=" + usuario.getTelefono()+"]";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	/*
	 * Devuelve true si el contacto pertenece a un usuario en concreto
	 */
	public boolean perteneceaUsuarioConTelefono(String telefono) {
		return usuario.getTelefono().equals(telefono);
	}
	
	/*
	 * Devuelve el contacto gemelo en el receptor
	 */
	public ContactoIndividual contactoEnElReceptor(String telefonoEmisor) {
		return usuario.buscarContactoPorTelefono(telefonoEmisor);
	}
	
	/**
	 * Verifica si el contacto tiene un nombre asignado
	 */
	public boolean tieneNombre() {
		return getNombre() != null && !getNombre().trim().isEmpty();
	}

	
	/**
	 * Obtiene el número de teléfono del usuario asociado a este contacto
	 */
	public String getTelefonoUsuario() {
		return usuario != null ? usuario.getTelefono() : null;
	}

	public ContactoIndividual crearContactoEnElReceptor(Usuario usuarioEmisor) {
		//DONE Crear mi contacto con el nombre vacio
		return usuario.addContactoIndividual("", usuarioEmisor);
	}

}
