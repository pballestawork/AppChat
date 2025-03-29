package persistencia.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import beans.Entidad;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import utils.FactoriaPruebas;

public class AdaptadorUsuarioDAOTest {
	private static ServicioPersistencia servicioPersistencia;
	private static IAdaptadorUsuarioDAO adaptadorUsuarioDAO;
	private static IAdaptadorContactoIndividualDAO adaptadorContactoDAO;

	private Usuario usuarioPablo;
	private Usuario usuarioAlvaro;

	@BeforeClass
	public static void setup() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();

		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();// TODO quitar o se borrara todo
		entidades.stream().forEach(e -> servicioPersistencia.borrarEntidad(e));

		try {
			FactoriaDAO factoriaDAO = FactoriaDAO.getInstancia();
			adaptadorUsuarioDAO = factoriaDAO.getUsuarioDAO();
			adaptadorContactoDAO = factoriaDAO.getContactoIndividualDAO();

		} catch (DAOException e) {
			System.out.println(e.getMessage());
		}
	}

	// Inicializar database
	@Before
	public void beforeEach() {
		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();// TODO quitar o se borrara todo
		entidades.stream().forEach(e -> servicioPersistencia.borrarEntidad(e));

		// 1. registrar usuarios
		usuarioPablo = FactoriaPruebas.crearUsuario();
		usuarioPablo.setNombre("Pablo");

		usuarioAlvaro = FactoriaPruebas.crearUsuario();
		usuarioAlvaro.setNombre("Alvaro");

		adaptadorUsuarioDAO.add(usuarioPablo);
		adaptadorUsuarioDAO.add(usuarioAlvaro);
	}

	@Test(expected = NullPointerException.class)
	public void agregarUsuarioNulo() {
		Usuario usuarioNulo = null;

		adaptadorUsuarioDAO.add(usuarioNulo);

		fail();
	}

	@Test
	public void agregarUsuarioCorrecto() {
		Usuario usuarioPrueba = FactoriaPruebas.crearUsuario();

		adaptadorUsuarioDAO.add(usuarioPrueba);
		assertNotEquals(0, usuarioPrueba.getId());
	}

	@Test
	public void agregarDosUsuarioCorrecto() {
		Usuario usuarioPrueba = FactoriaPruebas.crearUsuario();
		Usuario usuarioPrueba2 = FactoriaPruebas.crearUsuario();

		assertEquals(usuarioPrueba.getId(), 0);
		assertEquals(usuarioPrueba2.getId(), 0);

		adaptadorUsuarioDAO.add(usuarioPrueba);
		adaptadorUsuarioDAO.add(usuarioPrueba2);

		assertNotEquals(0, usuarioPrueba2.getId());
		assertNotEquals(0, usuarioPrueba.getId());
		assertNotEquals(usuarioPrueba.getId(), usuarioPrueba2.getId());
	}

	@Test
	public void agregarUsuarioDuplicado() {
		adaptadorUsuarioDAO.add(usuarioPablo);

		usuarioAlvaro.setId(usuarioPablo.getId());
		adaptadorUsuarioDAO.add(usuarioAlvaro);

		// Recuperamos el usuario que no deberia existir
		Usuario usuario = adaptadorUsuarioDAO.getById(usuarioAlvaro.getId());

		assertEquals(usuarioPablo, usuario);

		assertNotEquals(usuarioAlvaro, usuario);
		assertNotEquals(usuarioPablo, usuarioAlvaro);
	}

	@Test
	public void recuperarUsuarioNoExiste() {
		Usuario usuario = adaptadorUsuarioDAO.getById(0);
		assertNull(usuario);
	}

	@Test
	public void recuperarUsuarioConExito() {

		Usuario usuario = adaptadorUsuarioDAO.getById(usuarioPablo.getId());
		
		assertEquals(usuarioPablo, usuario);
		assertEquals(usuarioPablo.getContactos().size(), usuario.getContactos().size());
		assertEquals(usuarioPablo.getContrasena(), usuario.getContrasena());
		assertEquals(usuarioPablo.getEmail(), usuario.getEmail());
		assertEquals(usuarioPablo.getNombre(), usuario.getNombre());
		assertEquals(usuarioPablo.getFotoPerfil(), usuario.getFotoPerfil());
		assertEquals(usuarioPablo.getSaludo(), usuario.getSaludo());
		
		PoolDAO pool = PoolDAO.getUnicaInstancia();
		Usuario usuarioPool = (Usuario) pool.getObjeto(usuarioPablo.getId());
		
		assertEquals(usuario, usuarioPool);
	}
	
	@Test
	public void recuperarUsuarioConContactos() {
		PoolDAO pool = PoolDAO.getUnicaInstancia();
		pool.removeObject(usuarioPablo.getId());
		
		ContactoIndividual contactoIndividual = FactoriaPruebas.crearContactoIndividual(usuarioPablo);
		usuarioPablo.addContacto(contactoIndividual);
		adaptadorContactoDAO.add(contactoIndividual);
		adaptadorUsuarioDAO.update(usuarioPablo);
		
		
		Usuario usuario = adaptadorUsuarioDAO.getById(usuarioPablo.getId());
	
		assertEquals(usuarioPablo,usuario);
		assertTrue(usuarioPablo.getContactos().equals(usuario.getContactos()));
	}

	@Test
	public void actualizarUsuarioConExito() {
		// Obtener el usuario desde la base de datos
		Usuario usuario = adaptadorUsuarioDAO.getById(usuarioPablo.getId());

		// Modificar los campos del usuario
		usuario.setSaludo("Nuevo saludo");
		usuario.setNombre("LucasMarcos");
		usuario.setTelefono("987654321");
		usuario.setEmail("lucasmarcos@email.com");
		usuario.setContrasena("nuevaPassword123");
		usuario.setFotoPerfil("nuevaFoto.jpg");
		usuario.setEsPremium(true); // Convertir a premium

		// Guardar cambios en la base de datos
		adaptadorUsuarioDAO.update(usuario);

		// Recuperar el usuario actualizado desde la base de datos
		Usuario usuario2 = adaptadorUsuarioDAO.getById(usuarioPablo.getId());

		// Verificar que los cambios se han guardado correctamente
		assertEquals("Nuevo saludo", usuario2.getSaludo());
		assertEquals("LucasMarcos", usuario2.getNombre());
		assertEquals("987654321", usuario2.getTelefono());
		assertEquals("lucasmarcos@email.com", usuario2.getEmail());
		assertEquals("nuevaPassword123", usuario2.getContrasena());
		assertEquals("nuevaFoto.jpg", usuario2.getFotoPerfil());
		assertTrue(usuario2.isEsPremium()); // Verificar que es premium

		// Verificar que los contactos no han cambiado
		assertEquals(usuario.getContactos(), usuario2.getContactos());
	}

	
 	@Test
	public void actualizarUsuarioNoExiste() {
		Usuario usuario = FactoriaPruebas.crearUsuario();
		adaptadorUsuarioDAO.update(usuario);
	}
	
	@Test
	public void actualizarUsuarioSinPoolDAO() {
		// Obtener el usuario desde la base de datos
		Usuario usuario = adaptadorUsuarioDAO.getById(usuarioPablo.getId());
		PoolDAO pool = PoolDAO.getUnicaInstancia();
		pool.removeObject(usuario.getId());
		
		// Modificar los campos del usuario
		usuario.setSaludo("Nuevo saludo");
		usuario.setNombre("LucasMarcos");
		usuario.setTelefono("987654321");
		usuario.setEmail("lucasmarcos@email.com");
		usuario.setContrasena("nuevaPassword123");
		usuario.setFotoPerfil("nuevaFoto.jpg");
		usuario.setEsPremium(true); // Convertir a premium

		// Guardar cambios en la base de datos
		adaptadorUsuarioDAO.update(usuario);

		// Recuperar el usuario actualizado desde la base de datos
		Usuario usuario2 = adaptadorUsuarioDAO.getById(usuarioPablo.getId());
		
		// Verificar que los cambios se han guardado correctamente
		assertEquals("Nuevo saludo", usuario2.getSaludo());
		assertEquals("LucasMarcos", usuario2.getNombre());
		assertEquals("987654321", usuario2.getTelefono());
		assertEquals("lucasmarcos@email.com", usuario2.getEmail());
		assertEquals("nuevaPassword123", usuario2.getContrasena());
		assertEquals("nuevaFoto.jpg", usuario2.getFotoPerfil());
		assertTrue(usuario2.isEsPremium()); // Verificar que es premium

		// Verificar que los contactos no han cambiado
		assertEquals(usuario.getContactos(), usuario2.getContactos());
	}
	
	@Test
	public void eliminarUsuarioConExito() {
		adaptadorUsuarioDAO.getById(usuarioPablo.getId());//Para que entre al pool 
		PoolDAO pool = PoolDAO.getUnicaInstancia();
		Usuario u = (Usuario) pool.getObjeto(usuarioPablo.getId());
		assertEquals(u, usuarioPablo);
		
		adaptadorUsuarioDAO.delete(usuarioPablo);
		//Se debe eliminar del poolDAO y de la base de datos
		u = adaptadorUsuarioDAO.getById(usuarioPablo.getId());
		assertNull(u);	
		u = (Usuario) pool.getObjeto(usuarioPablo.getId());
		assertNull(u);
	}
	
	 @Test
	 public void eliminarUsuarioNoExiste() {
		 Usuario u = FactoriaPruebas.crearUsuario();
		 adaptadorUsuarioDAO.delete(u);
	 }
	 
	 
	 @Test
	 public void recuperarTodosUsuarios() {
		 List<Usuario> usuarios = adaptadorUsuarioDAO.getAll();
		 List<Usuario> usuariosIniciales = List.of(usuarioPablo,usuarioAlvaro);
		 assertEquals(usuariosIniciales, usuarios);
	 }

}
