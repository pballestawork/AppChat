package dominio.modelo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UsuarioTest {
	
	private Usuario usuarioConParametros;
	private Usuario usuario;

	@Before
	public void setup() {
		usuarioConParametros = new Usuario(6, "nombre", "telefono", "email", "contrasena", "fotoPerfil", true);
		usuario = new Usuario();
	}

	@Test
	public void constructorExito() {
		assertTrue(usuario.getId() == 0);
	}

	@Test
	public void constructorExitoConParametros() {
		assertTrue(usuarioConParametros.getId() == 6);
		assertTrue(usuarioConParametros.getNombre() == "nombre");
		assertTrue(usuarioConParametros.getTelefono() == "telefono");
		assertTrue(usuarioConParametros.getEmail() == "email");
		assertTrue(usuarioConParametros.getContrasena() == "contrasena");
		assertTrue(usuarioConParametros.getFotoPerfil() == "fotoPerfil");
		assertTrue(usuarioConParametros.isEsPremium() == true);
	}

}
