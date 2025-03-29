package persistencia.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import beans.Entidad;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import dominio.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import utils.FactoriaPruebas;

public class AdaptadorGrupoDAOTest {

	private static ServicioPersistencia servicioPersistencia;
	private static IAdaptadorUsuarioDAO adaptadorUsuarioDAO;
	private static IAdaptadorGrupoDAO adaptadorGrupoDAO;
	private static IAdaptadorContactoIndividualDAO adaptadorContactoDAO;

	private Usuario usuario;
	private ContactoIndividual contactoIndividual;
	private Grupo grupo;

	@BeforeClass
	public static void setup() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();
		entidades.forEach(e -> servicioPersistencia.borrarEntidad(e));

		try {
			FactoriaDAO factoriaDAO = FactoriaDAO.getInstancia();
			adaptadorUsuarioDAO = factoriaDAO.getUsuarioDAO();
			adaptadorGrupoDAO = factoriaDAO.getGrupoDAO();
			adaptadorContactoDAO = factoriaDAO.getContactoIndividualDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void beforeEach() {
		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();
		entidades.forEach(e -> servicioPersistencia.borrarEntidad(e));

		usuario = FactoriaPruebas.crearUsuario();
		adaptadorUsuarioDAO.add(usuario);

		contactoIndividual = FactoriaPruebas.crearContactoIndividual(usuario);
		adaptadorContactoDAO.add(contactoIndividual);

		grupo = new Grupo(0, "Grupo Test", List.of(contactoIndividual), "imagen.jpg");
		adaptadorGrupoDAO.add(grupo);
	}

	@Test(expected = NullPointerException.class)
	public void agregarGrupoNulo() {
		Grupo grupoNulo = null;
		adaptadorGrupoDAO.add(grupoNulo);
		fail();
	}

	@Test
	public void agregarGrupoConExito() {
		Grupo g = new Grupo(0, "Nuevo Grupo", List.of(contactoIndividual), "foto.png");
		assertEquals(0, g.getId());

		adaptadorGrupoDAO.add(g);
		assertNotEquals(0, g.getId());
	}

	@Test
	public void agregarGrupoDuplicado() {
		Grupo grupoDuplicado = new Grupo(grupo.getId(), "Otro nombre", List.of(contactoIndividual), "otra.png");
		adaptadorGrupoDAO.add(grupoDuplicado);

		Grupo recuperado = adaptadorGrupoDAO.getById(grupo.getId());
		assertEquals(grupo.getNombre(), recuperado.getNombre());
		assertNotEquals(grupoDuplicado.getNombre(), recuperado.getNombre());
	}

	@Test
	public void recuperarGrupoConExito() {
		Grupo recuperado = adaptadorGrupoDAO.getById(grupo.getId());

		assertNotNull(recuperado);
		assertEquals(grupo.getNombre(), recuperado.getNombre());
		assertEquals(grupo.getImagen(), recuperado.getImagen());
		assertEquals(grupo.getMiembros(), recuperado.getMiembros());
		
		PoolDAO pool = PoolDAO.getUnicaInstancia();
		Grupo gPool = (Grupo) pool.getObjeto(grupo.getId());
		assertEquals(recuperado, gPool);
	}

	@Test
	public void recuperarGrupoNoExiste() {
		Grupo g = adaptadorGrupoDAO.getById(9999);
		assertNull(g);
	}

	@Test
	public void actualizarGrupo() {
		grupo.setNombre("Grupo Actualizado");
		grupo.setImagen("nuevaImagen.jpg");

		adaptadorGrupoDAO.update(grupo);
		Grupo recuperado = adaptadorGrupoDAO.getById(grupo.getId());

		assertEquals("Grupo Actualizado", recuperado.getNombre());
		assertEquals("nuevaImagen.jpg", recuperado.getImagen());
		assertEquals(grupo.getMiembros(), recuperado.getMiembros());
	}

	@Test
	public void actualizarGrupoNoExistente() {
		Grupo fantasma = new Grupo(0, "Falso", List.of(contactoIndividual), "img.png");
		adaptadorGrupoDAO.update(fantasma); // No debe lanzar error
	}

	@Test
	public void actualizarGrupoSinPoolDAO() {
		PoolDAO.getUnicaInstancia().removeObject(grupo.getId());

		grupo.setNombre("Sin Pool");
		grupo.setImagen("imgPool.jpg");
		adaptadorGrupoDAO.update(grupo);

		Grupo recuperado = adaptadorGrupoDAO.getById(grupo.getId());
		assertEquals("Sin Pool", recuperado.getNombre());
		assertEquals("imgPool.jpg", recuperado.getImagen());
	}

	@Test
	public void eliminarGrupoConExito() {
		adaptadorGrupoDAO.getById(grupo.getId());
		Grupo g = adaptadorGrupoDAO.getById(grupo.getId());
		Grupo gPool = (Grupo) PoolDAO.getUnicaInstancia().getObjeto(grupo.getId());
		assertNotNull(g);
		assertEquals(g, gPool);
		
		adaptadorGrupoDAO.delete(grupo);

		assertNull(adaptadorGrupoDAO.getById(grupo.getId()));
		assertNull(PoolDAO.getUnicaInstancia().getObjeto(grupo.getId()));
	}

	@Test
	public void eliminarGrupoNoExistente() {
		Grupo fantasma = new Grupo(9999, "No existe", List.of(contactoIndividual), "none.png");
		adaptadorGrupoDAO.delete(fantasma); // No debe fallar
	}

	@Test
	public void recuperarTodosLosGrupos() {
		Grupo g2 = new Grupo(0, "Otro grupo", List.of(contactoIndividual), "icon.png");
		adaptadorGrupoDAO.add(g2);

		List<Grupo> grupos = adaptadorGrupoDAO.getAll();
		assertTrue(grupos.contains(grupo));
		assertTrue(grupos.contains(g2));
	}
}
