package dominio.modelo;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ContactoIndividualTest {
	
	private ContactoIndividual contactoIndividualConParmetros;
	private ContactoIndividual contactoIndividual;
	private Usuario user;

	@Before
	public void setup() {
		user = new Usuario();
		user.setNombre("Pablo");
		contactoIndividualConParmetros = new ContactoIndividual(5, "nombre",user);
		contactoIndividual = new ContactoIndividual();
	}

	@Test
	public void constructorExito() {
		assertTrue(contactoIndividual.getId() == 0);
	}

	@Test
	public void constructorExitoConParametros() {
		assertTrue(contactoIndividualConParmetros.getId() == 5);
		assertTrue(contactoIndividualConParmetros.getNombre() == "nombre");
		assertTrue(contactoIndividualConParmetros.getUsuario().getNombre() == "Pablo"); 
	}
}
