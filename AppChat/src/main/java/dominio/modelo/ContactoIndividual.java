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
	
}
