package persistencia.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import beans.Entidad;
import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import utils.FactoriaPruebas;

public class AdaptadorMensajeDAOTest {
	private static ServicioPersistencia servicioPersistencia;
	private static IAdaptadorMensajeDAO adaptadorMensajeDAO;
	private Usuario usuarioPablo;
	private Usuario usuarioAlvaro;
	
	
	@BeforeClass
	public static void setup() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		
		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();//TODO quitar o se borrara todo
		entidades.stream().forEach(e -> servicioPersistencia.borrarEntidad(e));
		
		try {
			FactoriaDAO factoriaDAO = FactoriaDAO.getInstancia();
			
			adaptadorMensajeDAO = factoriaDAO.getMensajeDAO();
		} catch (DAOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Before
	public void beforeEach() {
		
		List<Entidad> entidades = servicioPersistencia.recuperarEntidades();//TODO quitar o se borrara todo
		entidades.stream().forEach(e -> servicioPersistencia.borrarEntidad(e));
		
		usuarioPablo = FactoriaPruebas.crearUsuarioCompleto();
		usuarioPablo.setNombre("Pablo");
		usuarioAlvaro = FactoriaPruebas.crearUsuarioCompleto();
		usuarioAlvaro.setNombre("Alvaro");
	}
	
	/*
	 * add
	 */
	/*
	 * getById
	 * - getMensajeNoExiste
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
	 */
	
	@Test
	public void testGetAllMensajesSinMensajes() {
		List<Mensaje> mensajes = adaptadorMensajeDAO.getAll();
		
		assertEquals(mensajes.size(), 0, mensajes.size());
	}
	
	@Test
	public void testGetAllMensajesConVariosMensajes() {
		List<Mensaje> mensajesInicial = adaptadorMensajeDAO.getAll();
		
		Mensaje m1 = new Mensaje(0, usuarioPablo, "hola", LocalDateTime.now(), true);
		Mensaje m2 = new Mensaje(0, usuarioAlvaro, "hey", LocalDateTime.now(), false);
		
		adaptadorMensajeDAO.add(m1);
		adaptadorMensajeDAO.add(m2);
		
		List<Mensaje> mensajesFinal = adaptadorMensajeDAO.getAll();
		Mensaje m1Recuperado = mensajesFinal.get(mensajesFinal.size()-2); 
		Mensaje m2Recuperado = mensajesFinal.get(mensajesFinal.size()-1);
		
		assertEquals(mensajesInicial.size()+2, mensajesFinal.size());
		
		//Mensaje Pablo correcto
		assertNotEquals(m1.getId(),m1Recuperado.getId());
		assertEquals(usuarioPablo, m1Recuperado.getEmisor());
		assertEquals(m1.getContenido(), m1Recuperado.getContenido());
		assertEquals(m1.getFechaEnvio(), m1Recuperado.getFechaEnvio());
		assertEquals(m1.getTipo(), m1Recuperado.getTipo());
		
		
		//Mensaje Alvaro correcto
		assertNotEquals(m2.getId(),m2Recuperado.getId());
		assertEquals(usuarioAlvaro, m2Recuperado.getEmisor());
		assertEquals(m2.getContenido(), m2Recuperado.getContenido());
		assertEquals(m2.getFechaEnvio(), m2Recuperado.getFechaEnvio());
		assertEquals(m2.getTipo(), m2Recuperado.getTipo());

	}
	
	@Test
	public void testDeleteMensajeConExito() {
		
	}
	
	@Test
	public void testDeleteMensajeNoExiste() {
		
	}
	
	@Test
	public void testUpdateMensajeConExito() {
		
	}
	
	@Test
	public void testUpdateMensajeDosVeces() {
		
	}
	
	@Test
	public void testUpdateMensajeNoExiste() {
		
	}
	
	@Test
	public void testGetMensajeNoExiste() {
		
	}
	
	@Test
	public void testAddMensajeDosMensajesConExito() {
	}
	
	@Test
	public void testAddMensajeNulo() {
		
	}

	@Test
	public void testAddMensajeConExito() {
		List<Contacto> contactos = new LinkedList<Contacto>();
		Usuario u = new Usuario(0,"pablo","","","","",true,"Hola", contactos);
		Mensaje m = new Mensaje(0,u,"Mensaje",LocalDateTime.now(),true);
		
		adaptadorMensajeDAO.add(m);
	
		assertTrue(m.getId() != 0);
	}
	
	@Test
	public void testAddMensajeDuplicado() {
		
	}
	
	@Test
	public void testGetMensajeConExito() {
		List<Contacto> contactos = new LinkedList<Contacto>();
		Usuario u = new Usuario(0,"pablo","","","","",true,"Hola", contactos);
		Mensaje m = new Mensaje(0,u,"Mensaje",LocalDateTime.now(),true);
		
		adaptadorMensajeDAO.add(m);
		Mensaje mensaje = adaptadorMensajeDAO.getById(m.getId());
		assertEquals(m, mensaje);//Compara ids
		assertEquals(m.getEmisor(), mensaje.getEmisor());
		assertEquals(m.getFechaEnvio(), mensaje.getFechaEnvio());
		assertEquals(m.getContenido(), mensaje.getContenido());
		assertEquals(m.getTipo(), mensaje.getTipo());
	}
	
}

