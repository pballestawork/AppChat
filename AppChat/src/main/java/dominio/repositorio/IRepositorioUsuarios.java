package dominio.repositorio;

import java.time.LocalDate;

import dominio.modelo.Usuario;

public interface IRepositorioUsuarios extends RepositorioString<Usuario>{
	
	public Usuario add(String nombre, String telefono, String email, String contrasena, String fotoPerfil,
			String saludo, LocalDate fechaNacimiento)throws RepositorioException;
}
