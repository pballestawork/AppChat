package model;

import java.util.List;

public class Grupo {
	
	private int id;
	private String nombreGrupo;
	private List<Usuario> miembros;
	private String imagenGrupo;
	
	public Grupo(int id, String nombreGrupo, List<Usuario> miembros, String imagenGrupo) {
		this.id = id;
		this.nombreGrupo = nombreGrupo;
		this.miembros = miembros;
		this.imagenGrupo = imagenGrupo;
	}

	public int getId() {
		return id;
	}

	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public List<Usuario> getMiembros() {
		return miembros;
	}

	public String getImagenGrupo() {
		return imagenGrupo;
	}
	
	public void añadirMiembro(Usuario usuario) {
		miembros.add(usuario);
	}
	
	public void eliminarMiembro(Usuario usuario) {
		miembros.remove(usuario);
	}
	

}
