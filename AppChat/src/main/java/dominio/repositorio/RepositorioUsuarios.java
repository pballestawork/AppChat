package dominio.repositorio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dominio.controlador.ChatControllerException;
import dominio.modelo.Usuario;
import persistencia.dao.DAOException;
import persistencia.dao.FactoriaDAO;
import persistencia.dao.IAdaptadorUsuarioDAO;

public class RepositorioUsuarios implements IRepositorioUsuarios{
	
	private static RepositorioUsuarios unicaInstancia = null;
	private Map<String, Usuario> entidades = new HashMap<>();
	
	private RepositorioUsuarios() throws ChatControllerException {
		FactoriaDAO dao;
		try {
			dao = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			throw new ChatControllerException("Comprueba tu conexión con la base de datos.");
		}
		IAdaptadorUsuarioDAO usuarioDAO = dao.getUsuarioDAO();
		
		for (Usuario usuario : usuarioDAO.getAll()) {
			entidades.put(usuario.getTelefono(), usuario);
		}
	}
	
	public static RepositorioUsuarios getUnicaInstancia() throws ChatControllerException {
		if(unicaInstancia == null) 
			unicaInstancia = new RepositorioUsuarios();
		return unicaInstancia;
	}
	
	@Override
	public Usuario add(String nombre, String telefono, String email, String contrasena, String fotoPerfil,
			String saludo, LocalDate fechaNacimiento) throws RepositorioException {
		
		Usuario u = new Usuario(0,nombre, telefono, email, contrasena, fotoPerfil, false ,saludo, fechaNacimiento);
		
		if (this.entidades.containsKey(u.getTelefono()))
			throw new RepositorioException(u.getTelefono() + " ya existe en el repositorio");
		
		this.entidades.put(u.getTelefono(), u);
		System.out.println("Entidad " + u + " registrada");
		return u;
	}
	
	@Override
	public String add(Usuario entity) throws RepositorioException {
		Usuario u = add(entity.getNombre(),entity.getTelefono(),entity.getEmail(),entity.getContrasena(),entity.getFotoPerfil(),entity.getSaludo(), entity.getFechaNacimiento());
		return u.getTelefono(); 
	}

	@Override
	public void update(Usuario entity) throws RepositorioException, EntidadNoEncontrada {
		if (!this.entidades.containsKey(entity.getTelefono()))
			throw new EntidadNoEncontrada(entity.getTelefono() + " no existe en el repositorio");

		this.entidades.put(entity.getTelefono(), entity);	
	}

	@Override
	public void delete(Usuario entity) throws RepositorioException, EntidadNoEncontrada {
		if (!this.entidades.containsKey(entity.getTelefono()))
			throw new EntidadNoEncontrada(entity.getTelefono() + " no existe en el repositorio");

		this.entidades.remove(entity.getTelefono());
		
	}

	@Override
	public Usuario getById(String id) throws EntidadNoEncontrada {
		if (!this.entidades.containsKey(id))
			throw new EntidadNoEncontrada(id + " no existe en el repositorio");

		return this.entidades.get(id);
	}

	@Override
	public List<Usuario> getAll() throws RepositorioException {
		return new ArrayList<>(this.entidades.values());
	}

	@Override
	public List<String> getIds() throws RepositorioException {
		return new ArrayList<>(this.entidades.keySet());
	}
		
}
