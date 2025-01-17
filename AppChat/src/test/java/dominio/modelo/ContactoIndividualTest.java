package dominio.modelo;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ContactoIndividualTest {
	
	private ContactoIndividual contactoIndividualConParmetros;
	private ContactoIndividual contactoIndividual;

	@Before
	public void setup() {
		contactoIndividualConParmetros = new ContactoIndividual(5, "nombre","69", "imagen");
		contactoIndividual = new ContactoIndividual();
	}

	@Test
	public void constructorExito() {
		assertTrue(contactoIndividual.getId() == 0);
	}

	@Test
	public void constructorExitoConParametros() {
		assertTrue(contactoIndividualConParmetros.getId() == 5);
		assertTrue(contactoIndividualConParmetros.getNombreContacto() == "nombre");
		assertTrue(contactoIndividualConParmetros.getTelefonoContacto() == "69");
		assertTrue(contactoIndividualConParmetros.getImagenContacto() == "imagen");
	}

}
