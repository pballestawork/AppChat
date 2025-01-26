package dominio.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GrupoTest extends ContactoTest {

    private static final String IMAGEN_GRUPO = "grupo.png";
    private static final String NOMBRE_MIEMBRO = "Pedro";
    private static final String TELEFONO_MIEMBRO = "700800900";

    private Grupo grupo;
    private List<ContactoIndividual> miembros;

    @Before
    @Override
    public void beforeEach() {
        super.beforeEach(); // Llamar a la inicialización de ContactoTest
        miembros = new LinkedList<ContactoIndividual>();
        ContactoIndividual miembro = new ContactoIndividual(ID_CONTACTO, NOMBRE_MIEMBRO, new Usuario(ID_CONTACTO, NOMBRE_MIEMBRO, TELEFONO_MIEMBRO, null, null, null, false, null, null));
        miembros.add(miembro);
        grupo = new Grupo(ID_CONTACTO, NOMBRE_CONTACTO, miembros, IMAGEN_GRUPO);
    }

    @Test
    public void testConstructorVacioConExito() {
        Grupo g = new Grupo();
        assertTrue(g.getId() == 0);
        assertTrue(g.getNombre() == null);
        assertTrue(g.getMensajes() == null);
        assertTrue(g.getMiembros() == null);
        assertTrue(g.getImagen() == null);
    }

    @Test
    public void testConstructorParametrosConExito() {
        Grupo g = new Grupo(2, "Grupo Amigos", miembros, "amigos.png");
        assertTrue(g.getId() == 2);
        assertTrue(g.getNombre().equals("Grupo Amigos"));
        assertTrue(g.getMiembros().equals(miembros));
        assertTrue(g.getImagen().equals("amigos.png"));
    }

    @Test
    public void testSettersConExito() {
        grupo.setId(10);
        grupo.setNombre("Grupo Familia");
        List<ContactoIndividual> nuevosMiembros = new LinkedList<ContactoIndividual>();;
        ContactoIndividual nuevoMiembro = new ContactoIndividual(3, "Ana", new Usuario(3, "Ana", "123456789", null, null, null, false, null, null));
        nuevosMiembros.add(nuevoMiembro);
        grupo.setMiembros(nuevosMiembros);
        grupo.setImagen("familia.png");

        assertTrue(grupo.getId() == 10);
        assertTrue(grupo.getNombre().equals("Grupo Familia"));
        assertTrue(grupo.getMiembros().equals(nuevosMiembros));
        assertTrue(grupo.getImagen().equals("familia.png"));
    }

    @Test
    public void testAddMiembroConExito() {
        ContactoIndividual nuevoMiembro = new ContactoIndividual(4, "Luis", new Usuario(4, "Luis", "987654321", null, null, null, false, null, null));
        grupo.addMiembro(nuevoMiembro);

        assertTrue(grupo.getMiembros().contains(nuevoMiembro));
        assertTrue(grupo.getMiembros().size() == 2);
    }

    @Test
    public void testDeleteMiembroConExito() {
        ContactoIndividual miembro = miembros.get(0);
        grupo.deleteMiembro(miembro);

        assertFalse(grupo.getMiembros().contains(miembro));
        assertTrue(grupo.getMiembros().isEmpty());
    }

    @Test
    public void testToStringConExito() {
        String toStringDeberiaMostrar = "Grupo [nombre= " + NOMBRE_CONTACTO + ", miembros=" + miembros + "]";
        assertEquals(toStringDeberiaMostrar, grupo.toString());
    }

    @Test
    public void testEqualsConGrupoDuplicado() {
        Grupo grupoDuplicado = new Grupo(ID_CONTACTO, "", null, "");
        assertEquals(grupo, grupoDuplicado);
    }
}
