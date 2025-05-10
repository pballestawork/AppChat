package dominio.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.h2.constant.SysProperties;
import org.junit.Before;
import org.junit.Test;

public class UsuarioTest {

	private static final int ID_USUARIO = 1;
	private static final String NOMBRE_USUARIO = "Pablo";
	private static final String TELEFONO_USUARIO = "600600600";
	private static final String EMAIL_USUARIO = "pablo@gmail.com";
	private static final String CONTRASENA_USUARIO = "contrasena";
	private static final String FOTO_USUARIO = "fotoPerfil.png";
	private static final boolean PREMIUM_USUARIO = false;
	private static final String SALUDO_USUARIO = "Hola, soy Pablo";
	private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1999, 1, 1);

	private Usuario usuario;
	private List<Contacto> contactos;

	@Before
	public void beforeEach() {
		contactos = new LinkedList<Contacto>();
		contactos.add(new ContactoIndividual(2, "Pedro", null));
		usuario = new Usuario(ID_USUARIO, NOMBRE_USUARIO, TELEFONO_USUARIO, EMAIL_USUARIO, CONTRASENA_USUARIO,
				FOTO_USUARIO, PREMIUM_USUARIO, SALUDO_USUARIO,FECHA_NACIMIENTO, contactos);
	}

	@Test
	public void testConstructorVacioConExito() {
		Usuario u = new Usuario();
		assertTrue(u.getId() == 0);
		assertTrue(u.getNombre() == null);
		assertTrue(u.getTelefono() == null);
		assertTrue(u.getEmail() == null);
		assertTrue(u.getContrasena() == null);
		assertTrue(u.getFotoPerfil() == null);
		assertTrue(u.getSaludo() == null);
		assertTrue(u.getFechaNacimiento() == null);
		assertFalse(u.isEsPremium());
		assertTrue(u.getContactos().isEmpty());
	}

	@Test
	public void testConstructorParametrosConExito() {
		Usuario u = new Usuario(2, "Ana", "700800900", "ana@gmail.com", "password", "foto.png", true, "Hola!",LocalDate.of(1999, 1, 1),
				contactos);
		assertTrue(u.getId() == 2);
		assertTrue(u.getNombre().equals("Ana"));
		assertTrue(u.getTelefono().equals("700800900"));
		assertTrue(u.getEmail().equals("ana@gmail.com"));
		assertTrue(u.getContrasena().equals("password"));
		assertTrue(u.getFotoPerfil().equals("foto.png"));
		assertTrue(u.getSaludo().equals("Hola!"));
		assertTrue(u.isEsPremium());
		assertTrue(u.getContactos().equals(contactos));
	}

	@Test
	public void testSettersConExito() {
		usuario.setId(10);
		usuario.setNombre("Luis");
		usuario.setTelefono("800900100");
		usuario.setEmail("luis@gmail.com");
		usuario.setContrasena("newpass");
		usuario.setFotoPerfil("newFoto.png");
		usuario.setSaludo("Hola, soy Luis");
		usuario.setEsPremium(true);
		List<Contacto> nuevosContactos = new LinkedList<Contacto>();
		nuevosContactos.add(new ContactoIndividual(3, "Ana", null));
		usuario.setContactos(nuevosContactos);

		assertTrue(usuario.getId() == 10);
		assertTrue(usuario.getNombre().equals("Luis"));
		assertTrue(usuario.getTelefono().equals("800900100"));
		assertTrue(usuario.getEmail().equals("luis@gmail.com"));
		assertTrue(usuario.getContrasena().equals("newpass"));
		assertTrue(usuario.getFotoPerfil().equals("newFoto.png"));
		assertTrue(usuario.getSaludo().equals("Hola, soy Luis"));
		assertTrue(usuario.isEsPremium());
		assertTrue(usuario.getContactos().equals(nuevosContactos));
	}

	@Test
	public void testConvertirAPremiumConExito() {
		usuario.convertirAPremium();
		assertTrue(usuario.isEsPremium());
	}

	@Test
	public void testActualizarPerfilConExito() {
		usuario.actualizarPerfil("Carlos", "carlos@gmail.com", "fotoCarlos.png");

		assertTrue(usuario.getNombre().equals("Carlos"));
		assertTrue(usuario.getEmail().equals("carlos@gmail.com"));
		assertTrue(usuario.getFotoPerfil().equals("fotoCarlos.png"));
	}


	@Test
	public void testEqualsConUsuarioDuplicado() {
		Usuario usuarioDuplicado = new Usuario(ID_USUARIO, "Otro", TELEFONO_USUARIO, "otro@gmail.com", "otraContrasena",
				"otraFoto.png", false, "Otro saludo", LocalDate.of(1990, 1, 1),null);
		assertEquals(usuario, usuarioDuplicado);
	}

	@Test
	public void testEqualsConUsuarioNulo() {
		assertFalse(usuario.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsConClasesDistintas() {
		assertFalse(usuario.equals("objetoDistinto"));
	}
}