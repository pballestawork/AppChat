package dominio.modelo;

import java.util.ArrayList;
import java.util.List;


public class Grupo extends Contacto {

	private List<ContactoIndividual> miembros;
	private String imagen;

	public Grupo(int id, String nombreGrupo, List<ContactoIndividual> miembros, String imagen) {
		super(id, nombreGrupo);
		this.miembros = miembros != null ? miembros : new ArrayList<>();
		this.imagen = imagen;
	}

	public Grupo() {
		super();
		this.miembros = new ArrayList<>();
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
	
	/**
	 * Verifica si un contacto es miembro del grupo
	 * @param contacto El contacto a verificar
	 * @return true si el contacto es miembro del grupo, false en caso contrario
	 */
	public boolean esMiembro(ContactoIndividual contacto) {
		return contacto != null && miembros.contains(contacto);
	}
	
	/**
	 * Obtiene el número de miembros del grupo
	 * @return Cantidad de miembros
	 */
	public int getNumeroMiembros() {
		return miembros.size();
	}
}
