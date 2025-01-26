package dominio.modelo;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class MensajeTest {

	private Mensaje mensajeConParametros;
	private Mensaje mensaje;
	private LocalDateTime date = LocalDateTime.now();
	private Usuario usuario = new Usuario(); 

	@Before
	public void setup() {
		mensajeConParametros = new Mensaje(2,usuario,"contenido",date,true);
		mensaje = new Mensaje();
	}

	@Test
	public void constructorExito() {
		assertTrue(mensaje.getId() == 0);
	}

	@Test
	public void constructorExitoConParametros() {
		assertTrue(mensajeConParametros.getId() == 2);
		assertTrue(mensajeConParametros.getEmisor().equals(usuario));
		assertTrue(mensajeConParametros.getTipo() == true);
		assertTrue(mensajeConParametros.getContenido() == "contenido");
		assertTrue(mensajeConParametros.getFechaEnvio().equals(date));
	}
}
