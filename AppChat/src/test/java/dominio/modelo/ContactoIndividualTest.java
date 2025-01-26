package dominio.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ContactoIndividualTest extends ContactoTest {

    private static final String TELEFONO_USUARIO = "600700800";
    private static final String NOMBRE_USUARIO = "Luis";
    private static final String CORREO_USUARIO = "luis@gmail.com";
    private static final String CONTRASENA_USUARIO = "password";
    private static final String FOTO_USUARIO = "foto.png";
    private static final boolean PREMIUM_USUARIO = true;
    private static final String SALUDO_USUARIO = "Hola!";

    private Usuario usuario;
    private ContactoIndividual contactoIndividual;

    @Before
    @Override
    public void beforeEach() {
        super.beforeEach(); // Llamar a la inicialización de ContactoTest
        usuario = new Usuario(ID_CONTACTO, NOMBRE_USUARIO, TELEFONO_USUARIO, CORREO_USUARIO, CONTRASENA_USUARIO, FOTO_USUARIO, PREMIUM_USUARIO, SALUDO_USUARIO, null);
        contactoIndividual = new ContactoIndividual(ID_CONTACTO, NOMBRE_CONTACTO, usuario);
    }

    @Test
    public void testConstructorVacioConExito() {
        ContactoIndividual c = new ContactoIndividual();
        assertTrue(c.getId() == 0);
        assertTrue(c.getNombre() == null);
        assertTrue(c.getMensajes() == null);
        assertTrue(c.getUsuario() == null);
    }

    @Test
    public void testConstructorParametrosConExito() {
        ContactoIndividual c = new ContactoIndividual(2, "Maria", usuario);
        assertTrue(c.getId() == 2);
        assertTrue(c.getNombre().equals("Maria"));
        assertTrue(c.getUsuario().equals(usuario));
    }

    @Test
    public void testSettersConExito() {
        contactoIndividual.setId(10);
        contactoIndividual.setNombre("Carlos");
        Usuario nuevoUsuario = new Usuario(2, "Ana", "123456789", "ana@gmail.com", "newpass", "newfoto.png", false, "Hola", null);
        contactoIndividual.setUsuario(nuevoUsuario);

        assertTrue(contactoIndividual.getId() == 10);
        assertTrue(contactoIndividual.getNombre().equals("Carlos"));
        assertTrue(contactoIndividual.getUsuario().equals(nuevoUsuario));
    }

    @Test
    public void testToStringConExito() {
        String toStringDeberiaMostrar = "ContactoIndividual [nombre= " + NOMBRE_CONTACTO + ", usuario=" + TELEFONO_USUARIO + "]";
        assertEquals(toStringDeberiaMostrar, contactoIndividual.toString());
    }

    @Test
    public void testEqualsConContactoIndividualDuplicado() {
        ContactoIndividual contactoDuplicado = new ContactoIndividual(ID_CONTACTO,"", null);
        assertEquals(contactoIndividual, contactoDuplicado);
    }
}
