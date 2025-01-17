package dominio.modelo;

import static org.junit.Assert.*;

import org.junit.Test;

public class ContactoTest {
	Contacto contactoConParmetros = new Contacto(5, "nombre", "imagen");
	Contacto c = new Contacto();

	@Test
	public void constructorExito() {	
		assertTrue(c.getId() == 0);
	}

	@Test
	public void constructorExitoConParametros() {
		assertTrue(contactoConParmetros.getId() == 5);
		assertTrue(contactoConParmetros.getNombreContacto() == "nombre");
		assertTrue(contactoConParmetros.getImagenContacto() == "imagen");

		contactoConParmetros.setId(20323);
		
	}
	
	@Test
	public void constructorExitoConParametros2() {
		assertTrue(contactoConParmetros.getId() == 5);
		assertTrue(contactoConParmetros.getNombreContacto() == "nombre");
		assertTrue(contactoConParmetros.getImagenContacto() == "imagen");

		contactoConParmetros.setId(20323);
		
	}


}
