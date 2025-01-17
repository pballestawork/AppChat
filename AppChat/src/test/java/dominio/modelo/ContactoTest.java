package dominio.modelo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ContactoTest {
	private Contacto contactoConParmetros;
	private Contacto contacto;

	@Before
	public void setup() {
		contactoConParmetros = new Contacto(5, "nombre", "imagen");
		contacto = new Contacto();
	}

	@Test
	public void constructorExito() {
		assertTrue(contacto.getId() == 0);
	}

	@Test
	public void constructorExitoConParametros() {
		assertTrue(contactoConParmetros.getId() == 5);
		assertTrue(contactoConParmetros.getNombreContacto() == "nombre");
		assertTrue(contactoConParmetros.getImagenContacto() == "imagen");
	}
}
