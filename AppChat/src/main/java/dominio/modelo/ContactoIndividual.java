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
	
	/**
	 * Verifica si el contacto tiene un nombre asignado
	 * @return true si el contacto tiene nombre, false en caso contrario
	 */
	public boolean tieneNombre() {
		return getNombre() != null && !getNombre().trim().isEmpty();
	}

	
	
	/**
	 * Obtiene el número de teléfono del usuario asociado a este contacto
	 * @return El número de teléfono del usuario o null si no hay usuario asociado
	 */
	public String getTelefonoUsuario() {
		return usuario != null ? usuario.getTelefono() : null;
	}
}
