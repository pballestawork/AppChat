package persistencia.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import beans.Entidad;
import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import utils.FactoriaPruebas;

public class AdaptadorContactoIndividualDAOTest {
	private static ServicioPersistencia servicioPersistencia;
	private static IAdaptadorUsuarioDAO adaptadorUsuarioDAO;
	private static IAdaptadorContactoIndividualDAO adaptadorContactoDAO;

	private Usuario usuarioPablo;
	private ContactoIndividual contactoPablo;

	@BeforeClass
	public static void setup() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();

		// Limpiar todas las entidades antes de empezar
		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();
		entidades.forEach(e -> servicioPersistencia.borrarEntidad(e));

		try {
			FactoriaDAO factoriaDAO = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorUsuarioDAO = factoriaDAO.getUsuarioDAO();
			adaptadorContactoDAO = factoriaDAO.getContactoIndividualDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void beforeEach() {
		// Borrado de entidades antes de cada test
		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();
		entidades.forEach(e -> servicioPersistencia.borrarEntidad(e));

		// Crear usuario y contacto
		usuarioPablo = FactoriaPruebas.crearUsuario();
		adaptadorUsuarioDAO.add(usuarioPablo);

		contactoPablo = FactoriaPruebas.crearContactoIndividual(usuarioPablo);
	}

	@Test(expected = NullPointerException.class)
	public void agregarContactoNulo() {
	    ContactoIndividual contactoNulo = null;
	    adaptadorContactoDAO.add(contactoNulo);
	    fail(); // Nunca debería llegar aquí
	}
	
	@Test
	public void agregarContactoConExito() {
		assertEquals(0, contactoPablo.getId());
		adaptadorContactoDAO.add(contactoPablo);
		assertNotEquals(0, contactoPablo.getId());
	}
	
	@Test
	public void agregarContactoDuplicado() {
	    adaptadorContactoDAO.add(contactoPablo);
	    
	    Usuario usuario = FactoriaPruebas.crearUsuario();
	    ContactoIndividual contactoDuplicado = FactoriaPruebas.crearContactoIndividual(usuario);
	    contactoDuplicado.setId(contactoPablo.getId());

	    adaptadorContactoDAO.add(contactoDuplicado);

	    // Se espera que no reemplace el original
	    ContactoIndividual contacto = adaptadorContactoDAO.getById(contactoPablo.getId());

	    assertEquals(contactoPablo.getNombre(), contacto.getNombre());
	    assertNotEquals(contacto.getNombre(), contactoDuplicado.getNombre());
	    assertNotEquals(contacto.getUsuario(), contactoDuplicado.getUsuario());
	    
	}

	@Test
	public void recuperarContactoConExito() {
		adaptadorContactoDAO.add(contactoPablo);

		ContactoIndividual c = adaptadorContactoDAO.getById(contactoPablo.getId());

		assertNotNull(c);
		assertEquals(contactoPablo.getNombre(), c.getNombre());
		assertEquals(contactoPablo.getUsuario(), c.getUsuario());
		assertEquals(contactoPablo.getMensajes(), c.getMensajes());
		
		PoolDAO pool = PoolDAO.getUnicaInstancia();
		ContactoIndividual contactoPool = (ContactoIndividual) pool.getObjeto(contactoPablo.getId());
		assertEquals(c, contactoPool);
	}
	
	@Test
	public void recuperarContactoNoExiste() {
		Contacto c = adaptadorContactoDAO.getById(1);
		assertNull(c);
	}

	@Test
	public void actualizarContacto() {
		adaptadorContactoDAO.add(contactoPablo);
		contactoPablo.setNombre("Nombre actualizado");

		adaptadorContactoDAO.update(contactoPablo);
		ContactoIndividual c = adaptadorContactoDAO.getById(contactoPablo.getId());

		assertEquals("Nombre actualizado", c.getNombre());
		assertEquals(contactoPablo.getUsuario(), c.getUsuario());
		assertEquals(contactoPablo.getMensajes(), c.getMensajes());
	}

	@Test
	public void actualizarContactoNoExistente() {
	    ContactoIndividual contacto = FactoriaPruebas.crearContactoIndividual(usuarioPablo);
	    contacto.setNombre("Fantasma");
	    adaptadorContactoDAO.update(contacto); // No está en base de datos → no debería fallar
	}

	@Test
	public void actualizarContactoSinPoolDAO() {
	    adaptadorContactoDAO.add(contactoPablo);
	    PoolDAO pool = PoolDAO.getUnicaInstancia();
	    pool.removeObject(contactoPablo.getId());

	    contactoPablo.setNombre("Nombre fuera del Pool");
	    adaptadorContactoDAO.update(contactoPablo);

	    ContactoIndividual recuperado = adaptadorContactoDAO.getById(contactoPablo.getId());
	    assertEquals("Nombre fuera del Pool", recuperado.getNombre());
	}
	
	@Test
	public void eliminarContacto() {
		adaptadorContactoDAO.add(contactoPablo);
		adaptadorContactoDAO.getById(contactoPablo.getId());//Para que entre al pool
		
		PoolDAO pool = PoolDAO.getUnicaInstancia();
		Contacto c = (ContactoIndividual) pool.getObjeto(contactoPablo.getId());
		assertEquals(c,contactoPablo);
		
		adaptadorContactoDAO.delete(contactoPablo);

		c = (ContactoIndividual) pool.getObjeto(contactoPablo.getId());
		assertNull(c);
		c = adaptadorContactoDAO.getById(contactoPablo.getId());
		assertNull(c);
	}
	
	@Test
	public void eliminarContactoNoExistente() {
	    ContactoIndividual fantasma = FactoriaPruebas.crearContactoIndividual(usuarioPablo);
	    adaptadorContactoDAO.delete(fantasma); // No debería lanzar error
	}

	@Test
	public void recuperarTodosContactos() {
		ContactoIndividual contacto1 = FactoriaPruebas.crearContactoIndividual(usuarioPablo);
		ContactoIndividual contacto2 = FactoriaPruebas.crearContactoIndividual(usuarioPablo);

		adaptadorContactoDAO.add(contacto1);
		adaptadorContactoDAO.add(contacto2);

		List<ContactoIndividual> contactos = adaptadorContactoDAO.getAll();

		assertTrue(contactos.contains(contacto1));
		assertTrue(contactos.contains(contacto2));
	}

}
