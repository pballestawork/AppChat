package dominio.repositorio;

import dominio.modelo.Usuario;

public interface IRepositorioUsuarios extends RepositorioString<Usuario>{
	
	public Usuario add(String nombre, String telefono, String email, String contrasena, String fotoPerfil,
			String saludo) throws RepositorioException;
}
