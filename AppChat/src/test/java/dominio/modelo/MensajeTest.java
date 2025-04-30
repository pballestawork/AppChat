package dominio.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class MensajeTest {
	private static final int ID_USUARIO = 1;
	private static final String NOMBRE_USUARIO = "Pablo";
	private static final String TELEFONO_USUARIO = "600600600";
	private static final String CORREO_USUARIO = "pablo@gmail.com";
	private static final String CONTRASENA_USUARIO = "contrasena";
	private static final String FOTO_USUARIO = "fotoUsuario.png";
	private static final boolean PREMIUM_USUARIO = false;
	private static final String SALUDO_USUARIO = "Esto es un saludo";
	private static final LinkedList<Contacto> CONTACTOS = new LinkedList<Contacto>();
	private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1999, 1, 1);

	private Mensaje mensaje;
	private int idMensaje;
	private String contenido;
	private LocalDateTime fechaAhora;
	private Usuario usuario;
	private boolean tipo;
	private Contacto receptor;

	@Before
	public void beforeEach() {
		usuario = new Usuario(ID_USUARIO, NOMBRE_USUARIO, TELEFONO_USUARIO, CORREO_USUARIO, CONTRASENA_USUARIO, FOTO_USUARIO,
				PREMIUM_USUARIO, SALUDO_USUARIO, FECHA_NACIMIENTO,CONTACTOS);
		
		receptor = new ContactoIndividual(0, NOMBRE_USUARIO, usuario);
		
		contenido = "Mensaje beforeEach";
		fechaAhora = LocalDateTime.now().withNano(0);
		tipo = true;
		idMensaje = 1;
		mensaje = new Mensaje(idMensaje, usuario, receptor, contenido, fechaAhora, tipo);
	}

	@Test
	public void testConstructorVacioConExito() {
		Mensaje m = new Mensaje();
		assertTrue(m.getId() == 0);
		assertTrue(m.getEmisor() == null);
		assertTrue(m.getContenido() == null);
		assertTrue(m.getFechaEnvio() == null);
		assertTrue(m.getTipo() == null);
	}

	@Test
	public void testConstructorParametrosConExito() {
		String contenidoMensaje = "Mensaje constructor";
		Mensaje m = new Mensaje(200, usuario, receptor, contenidoMensaje, fechaAhora, true);
		assertTrue(m.getId() == 200);
		assertTrue(m.getEmisor().equals(usuario));
		assertTrue(m.getContenido().equals(contenidoMensaje));
		assertTrue(m.getFechaEnvio().isEqual(fechaAhora));
		assertTrue(m.getTipo() == true);
	}
	
	@Test
	public void testSettersConExito() {
		Usuario u = new Usuario(20,"","","","","",true,"",LocalDate.of(1999, 1, 1),null);
		LocalDateTime f = LocalDateTime.now().plusMinutes(100);
		
		mensaje.setId(1000);
		mensaje.setEmisor(u);
		mensaje.setContenido("contenido");
		mensaje.setFechaEnvio(f);
		mensaje.setTipo(false);
		
		assertTrue(mensaje.getId() == 1000);
		assertTrue(mensaje.getEmisor().equals(u));
		assertTrue(mensaje.getContenido().equals("contenido"));
		assertTrue(mensaje.getFechaEnvio().isEqual(f));
		assertTrue(mensaje.getTipo() == false);
	}
	
	@Test
	public void testToStringConExito() {

		String toStringDeberiaMostrar= "Mensaje [emisor=" + mensaje.getEmisor().getTelefono() + ", contenido=" + contenido
				+ ", fechaEnvio=" + fechaAhora + "]";

		assertTrue(mensaje.toString().equals(toStringDeberiaMostrar));
	}
	
	@Test
	public void testEqualsConMensajeDuplicado() {
		Mensaje mensajeDuplicado = new Mensaje(idMensaje, usuario, receptor, contenido, fechaAhora, tipo);
		assertEquals(mensaje, mensajeDuplicado);	
	}
	
	@Test
	public void testEqualsConMensajesIguales() {
		assertEquals(mensaje, mensaje);	
	}

	@Test
	public void testEqualsConMensajeNulo() {
		assertFalse(mensaje.equals(null));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsConClasesDistintas() {
		assertFalse(mensaje.equals("objetoDistinto"));
	}

}
