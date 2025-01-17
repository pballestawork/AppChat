package dominio.modelo;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class GrupoTest {

	private Grupo grupoConParmetros;
	private Grupo grupo;
	private List<ContactoIndividual> lista = new LinkedList<ContactoIndividual>();

	@Before
	public void setup() {
		grupoConParmetros = new Grupo(3, "nombre", lista, "imagen");
		grupo = new Grupo();
	}

	@Test
	public void constructorExito() {
		assertTrue(grupo.getId() == 0);
	}

	@Test
	public void constructorExitoConParametros() {
		assertTrue(grupoConParmetros.getId() == 3);
		assertTrue(grupoConParmetros.getNombreContacto() == "nombre");
		assertTrue(grupoConParmetros.getImagenContacto() == "imagen");
		assertTrue(grupoConParmetros.getMiembros().equals(lista));
	}

}
