package model;

import java.util.List;

public class Grupo extends Contacto {

	private String nombreGrupo;
	private List<ContactoIndividual> miembros;

	public Grupo(int id, String nombreGrupo, List<ContactoIndividual> miembros, String imagenGrupo) {
		super(id, nombreGrupo, imagenGrupo);
		this.nombreGrupo = nombreGrupo;
		this.miembros = miembros;
	}

	public Grupo() {
		super();
	}

	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public List<ContactoIndividual> getMiembros() {
		return miembros;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	public void setMiembros(List<ContactoIndividual> miembros) {
		this.miembros = miembros;
	}

	public void añadirMiembro(ContactoIndividual usuario) {
		miembros.add(usuario);
	}

	public void eliminarMiembro(ContactoIndividual usuario) {
		miembros.remove(usuario);
	}

}
