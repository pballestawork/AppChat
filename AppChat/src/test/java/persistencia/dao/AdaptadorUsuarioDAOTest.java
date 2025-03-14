package persistencia.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
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

	/*
	 * add
	 * 
	 * - addMensajeNulo
	 * - addMensajeVacio
	 * - addMensajeDuplicado
	 * - addMensajeConExito
	 * - addMensajeDosMensajesConExito
	 * 
	 * getById
	 * - getMensajeNoExiste
	 * - getMensajeConExito
	 * 
	 * update
	 * - updateMensajeNoExiste
	 * - updateMensajeDosVeces
	 * - updateMensaje
	 * 
	 * delete
	 * 
	 * - deleteMensajeNoExiste
	 * - deleteMensaje
	 * 
	 * getAll
	 * - getAllMensajesSinMensajes
	 * - getAllMensajesConVariosMensajes
	 * 
	 * 
	 */
	
	@Test
	public void recuperarTodosUsuariosSinUsuarios() {
		List<Usuario> usuarios = adaptadorUsuarioDAO.getAll();
		assertEquals("La lista de usuario debería ser vacia inicialmente", 0, usuarios.size());
	}

	@Test
	public void agregarUsuarioCorrecto() throws DAOException {
		adaptadorUsuarioDAO.add(usuarioPablo);
		assertNotEquals(0, usuarioPablo.getId());
	}
	
	@Test
	public void agregarDosUsuarioCorrecto() throws DAOException {
		assertEquals(usuarioPablo.getId(), 0);
		assertEquals(usuarioJuan.getId(), 0);
		
		adaptadorUsuarioDAO.add(usuarioPablo);
		adaptadorUsuarioDAO.add(usuarioJuan);
		
		assertNotEquals(0, usuarioPablo.getId());
		assertNotEquals(0, usuarioJuan.getId());
		assertNotEquals(usuarioJuan.getId(), usuarioPablo.getId());
	}
		
	@Test
	public void recuperarUsuarioNoExiste() throws DAOException {
		Usuario usuario = adaptadorUsuarioDAO.getById(0);
		assertNull(usuario);
	}
	
	@Test
	public void recuperarUsuarioConExito() throws DAOException {
		adaptadorUsuarioDAO.add(usuarioPablo);
		
		Usuario usuario = adaptadorUsuarioDAO.getById(usuarioPablo.getId());
		
		assertEquals(usuarioPablo, usuario);
		assertEquals(usuarioPablo.getContactos().size(), usuario.getContactos().size());
		assertEquals(usuarioPablo.getContrasena(), usuario.getContrasena());
		assertEquals(usuarioPablo.getEmail(), usuario.getEmail());
		assertEquals(usuarioPablo.getNombre(), usuario.getNombre());
		assertEquals(usuarioPablo.getFotoPerfil(), usuario.getFotoPerfil());
		assertEquals(usuarioPablo.getSaludo(), usuario.getSaludo());
	}
	
	@Test
	public void agregarUsuarioDuplicado() throws DAOException {
		adaptadorUsuarioDAO.add(usuarioPablo);
		
		usuarioJuan.setId(usuarioPablo.getId());
		adaptadorUsuarioDAO.add(usuarioJuan);
		
		//Recuperamos el usuario que no deberia existir
		Usuario u = adaptadorUsuarioDAO.getById(usuarioJuan.getId());
		
		assertEquals(usuarioPablo, u);
		assertNotEquals(usuarioJuan, u);
		assertNotEquals(usuarioPablo, usuarioJuan);
	}

}

