package persistencia.dao;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import beans.Entidad;
import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import utils.FactoriaPruebas;

public class AdaptadorMensajeDAOTest {

	private static ServicioPersistencia servicioPersistencia;
	private static IAdaptadorUsuarioDAO adaptadorUsuarioDAO;
	private static IAdaptadorMensajeDAO adaptadorMensajeDAO;

	private Usuario emisor;
	private Mensaje mensaje;

	@BeforeClass
	public static void setup() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();

		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();
		entidades.forEach(e -> servicioPersistencia.borrarEntidad(e));

		try {
			FactoriaDAO factoriaDAO = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorUsuarioDAO = factoriaDAO.getUsuarioDAO();
			adaptadorMensajeDAO = factoriaDAO.getMensajeDAO();
		} catch (DAOException e) {
			fail(e.getMessage());
		}
	}

	@Before
	public void beforeEach() {
		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();
		entidades.forEach(e -> servicioPersistencia.borrarEntidad(e));

		emisor = FactoriaPruebas.crearUsuario();
		adaptadorUsuarioDAO.add(emisor);

		mensaje = new Mensaje(0, emisor, "Hola Mundo", LocalDateTime.now().withNano(0), true);
	}

	@Test(expected = NullPointerException.class)
	public void agregarMensajeNulo() {
		Mensaje mensajeNulo = null;
		adaptadorMensajeDAO.add(mensajeNulo);
		fail();
	}

	@Test
	public void agregarMensajeCorrecto() {
		assertEquals(0, mensaje.getId());
		adaptadorMensajeDAO.add(mensaje);
		assertNotEquals(0, mensaje.getId());
	}

	@Test
	public void agregarMensajeDuplicado() {
		adaptadorMensajeDAO.add(mensaje);

		Mensaje duplicado = new Mensaje(0, emisor, "Otro mensaje", LocalDateTime.now().withNano(0), false);
		duplicado.setId(mensaje.getId());

		adaptadorMensajeDAO.add(duplicado);

		Mensaje recuperado = adaptadorMensajeDAO.getById(mensaje.getId());
		assertEquals("Hola Mundo", recuperado.getContenido());
		assertNotEquals("Otro mensaje", recuperado.getContenido());
	}

	@Test
	public void recuperarMensajeConExito() {
		adaptadorMensajeDAO.add(mensaje);

		Mensaje recuperado = adaptadorMensajeDAO.getById(mensaje.getId());

		assertNotNull(recuperado);
		assertEquals(mensaje.getContenido(), recuperado.getContenido());
		assertEquals(mensaje.getEmisor(), recuperado.getEmisor());
		assertEquals(mensaje.getFechaEnvio(), recuperado.getFechaEnvio());
	}

	@Test
	public void recuperarMensajeNoExiste() {
		Mensaje m = adaptadorMensajeDAO.getById(1234);
		assertNull(m);
	}

	@Test
	public void actualizarMensajeConExito() {
		adaptadorMensajeDAO.add(mensaje);

		mensaje.setContenido("Mensaje actualizado");
		adaptadorMensajeDAO.update(mensaje);

		Mensaje actualizado = adaptadorMensajeDAO.getById(mensaje.getId());
		assertEquals("Mensaje actualizado", actualizado.getContenido());
	}

	@Test
	public void actualizarMensajeNoExiste() {
		Mensaje fantasma = new Mensaje(0, emisor, "Fantasma", LocalDateTime.now().withNano(0), true);
		adaptadorMensajeDAO.update(fantasma); // No debería fallar
	}

	@Test
	public void eliminarMensajeConExito() {
		adaptadorMensajeDAO.add(mensaje);
		assertNotNull(adaptadorMensajeDAO.getById(mensaje.getId()));

		adaptadorMensajeDAO.delete(mensaje);
		assertNull(adaptadorMensajeDAO.getById(mensaje.getId()));
	}

	@Test
	public void eliminarMensajeNoExistente() {
		Mensaje m = new Mensaje(99, emisor, "No existe", LocalDateTime.now().withNano(0), true);
		adaptadorMensajeDAO.delete(m); // No debería fallar
	}

	@Test
	public void recuperarTodosMensajes() {
		Mensaje m1 = new Mensaje(0, emisor, "M1", LocalDateTime.now().withNano(0), true);
		Mensaje m2 = new Mensaje(0, emisor, "M2", LocalDateTime.now().withNano(0), true);

		adaptadorMensajeDAO.add(m1);
		adaptadorMensajeDAO.add(m2);

		List<Mensaje> mensajes = adaptadorMensajeDAO.getAll();

		assertTrue(mensajes.contains(m1));
		assertTrue(mensajes.contains(m2));
	}
}
