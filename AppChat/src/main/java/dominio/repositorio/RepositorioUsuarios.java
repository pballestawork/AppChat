package dominio.repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dominio.controlador.ChatControllerException;
import dominio.modelo.Usuario;
import persistencia.dao.DAOException;
import persistencia.dao.FactoriaDAO;
import persistencia.dao.IAdaptadorUsuarioDAO;

public class RepositorioUsuarios implements RepositorioString<Usuario>{
	
	private static RepositorioUsuarios unicaInstancia = null;
	private HashMap<String, Usuario> entidades = new HashMap<>();
	
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
	public String add(Usuario entity) throws RepositorioException {
		if (this.entidades.containsKey(entity.getTelefono()))
			throw new RepositorioException(entity.getTelefono() + " ya existe en el repositorio");
		
		this.entidades.put(entity.getTelefono(), entity);
		System.out.println("Entidad " + entity + " registrada");
		return entity.getTelefono();
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
	public Usuario getById(String id) throws RepositorioException, EntidadNoEncontrada {
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
