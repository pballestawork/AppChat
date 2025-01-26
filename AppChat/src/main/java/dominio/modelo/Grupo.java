package dominio.modelo;

import java.util.List;
import java.util.Objects;

public class Grupo extends Contacto {

	private List<ContactoIndividual> miembros;
	private String imagen;

	public Grupo(int id, String nombreGrupo, List<ContactoIndividual> miembros, String imagen) {
		super(id, nombreGrupo);
		this.miembros = miembros;
		this.setImagen(imagen);
	}

	public Grupo() {
		super();
	}
	
	
	@Override
	public String toString() {
		return "Grupo [nombre= "+ super.getNombre() + ", miembros=" + miembros + "]";
	}
	
	
	/*
	 * Getters and setters
	 */

	public List<ContactoIndividual> getMiembros() {
		return miembros;
	}

	public void setMiembros(List<ContactoIndividual> miembros) {
		this.miembros = miembros;
	}

	public void addMiembro(ContactoIndividual usuario) {
		miembros.add(usuario);
	}

	public void deleteMiembro(ContactoIndividual usuario) {
		miembros.remove(usuario);
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

}
