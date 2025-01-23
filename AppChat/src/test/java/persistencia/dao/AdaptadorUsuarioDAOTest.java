package persistencia.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dominio.modelo.Usuario;

public class AdaptadorUsuarioDAOTest {
	private static IAdaptadorUsuarioDAO adaptadorUsuarioDAO;
	private Usuario usuarioPablo; 
	private Usuario usuarioJuan; 
	
	@BeforeClass
	public static void setup() {
		try {
			FactoriaDAO factoriaDAO = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorUsuarioDAO = factoriaDAO.getUsuarioDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void beforeEach() {
		usuarioPablo = new Usuario();
		usuarioPablo.setNombre("Pablo");
		usuarioPablo.setEmail("pablo@gmail.com");
		usuarioPablo.setTelefono("600600600");
			
		usuarioJuan = new Usuario();
		usuarioJuan.setNombre("Juan");
		usuarioJuan.setEmail("juan@gmail.com");
		usuarioJuan.setTelefono("900900900");
	}

	@Test
	public void recuperarTodosUsuariosSinUsuarios() {
		List<Usuario> usuarios = adaptadorUsuarioDAO.getAll();
		assertEquals("La lista de usuario debería ser vacia inicialmente", 0, usuarios.size());
	}

	@Test
	public void agregarUsuarioCorrecto() throws DAOException {
		int id = adaptadorUsuarioDAO.add(usuarioPablo);
		assertNotEquals(0, id);
	}
	
	@Test
	public void agregarDosUsuarioCorrecto() throws DAOException {
		assertEquals(usuarioPablo.getId(), 0);
		assertEquals(usuarioJuan.getId(), 0);
		
		int id = adaptadorUsuarioDAO.add(usuarioPablo);
		int id2 = adaptadorUsuarioDAO.add(usuarioJuan);
		
		assertEquals(usuarioPablo.getId(), id);
		assertEquals(usuarioJuan.getId(), id2);
		assertNotEquals(id, id2);
	}
	
	@Test(expected = DAOException.class)
	public void agregarUsuarioDuplicado() throws DAOException {
		int id = adaptadorUsuarioDAO.add(usuarioPablo);
		usuarioJuan.setId(id);
		adaptadorUsuarioDAO.add(usuarioJuan);
		fail("Debería haber saltado la excepción DAO por usuario existente");
	}
	
	@Test(expected = DAOException.class)
	public void recuperarUsuarioNoExiste() throws DAOException {
		Usuario usuario = adaptadorUsuarioDAO.getById(0);
		fail("Debería haber saltado una excepción DAO por no encontrar la entidad");
	}
	
	
	@Test
	public void recuperarUsuarioConExito() throws DAOException {
		int id = adaptadorUsuarioDAO.add(usuarioPablo);
		Usuario usuario = adaptadorUsuarioDAO.getById(id);		
		assertEquals(usuarioPablo, usuario);
	}
	

}

