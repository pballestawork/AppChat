package persistencia.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import beans.Entidad;
import dominio.modelo.Contacto;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorMensajeDAOTest {
	private static ServicioPersistencia servicioPersistencia;
	private static IAdaptadorMensajeDAO adaptadorMensajeDAO;
	
	private static List<Entidad> mensajesIniciales;
	private static int numInicialMensajes;
	
	
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
		
		mensajesIniciales = servicioPersistencia.recuperarEntidades(AdaptadorMensajeDAO.getUnicaInstancia().TIPO_MENSAJE);
		numInicialMensajes = mensajesIniciales.size();
	}
	
	@Before
	public void beforeEach() {
		
	}
	
	@Test
	public void testGetAllMensajesVacio() {
		List<Mensaje> mensajes = adaptadorMensajeDAO.getAll();
		
		assertEquals(mensajes.size(), numInicialMensajes, mensajes.size());
	}
	
	@Test
	public void testAgregarMensajeConExito() {
		List<Contacto> contactos = new LinkedList<Contacto>();
		Usuario u = new Usuario(0,"pablo","","","","",true,"Hola", contactos);
		Mensaje m = new Mensaje(0,u,"Mensaje",LocalDateTime.now(),true);
		
		adaptadorMensajeDAO.add(m);
	
		assertTrue(m.getId() != 0);
	}
	
	@Test
	public void testRecuperarMensajeConExito() {
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
