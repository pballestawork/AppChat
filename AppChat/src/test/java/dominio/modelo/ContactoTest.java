package dominio.modelo;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import utils.FactoriaPruebas;

public class ContactoTest {	
    protected static final int ID_CONTACTO = 1;
    protected static final String NOMBRE_CONTACTO = "Juan";
    private static final int ID_MENSAJE = 1;
    private static final String CONTENIDO_MENSAJE = "Hola";

    private Contacto contacto;
    private List<Mensaje> mensajes;

    @Before
    public void beforeEach() {
        mensajes = new LinkedList<Mensaje>();
        contacto = new Contacto(ID_CONTACTO, NOMBRE_CONTACTO);
        contacto.setMensajes(mensajes);
    }

    @Test
    public void testConstructorVacioConExito() {
        Contacto c = new Contacto();
        assertTrue(c.getId() == 0);
        assertTrue(c.getNombre() == null);
        assertTrue(c.getMensajes() == null);
    }

    @Test
    public void testConstructorParametrosConExito() {
        Contacto c = new Contacto(2, "Maria");
        assertTrue(c.getId() == 2);
        assertTrue(c.getNombre().equals("Maria"));
        assertTrue(c.getMensajes().isEmpty());
    }

    @Test
    public void testSettersConExito() {
        contacto.setId(10);
        contacto.setNombre("Carlos");
        List<Mensaje> nuevosMensajes = new ArrayList<>();
        contacto.setMensajes(nuevosMensajes);

        assertTrue(contacto.getId() == 10);
        assertTrue(contacto.getNombre().equals("Carlos"));
        assertTrue(contacto.getMensajes().equals(nuevosMensajes));
    }

    @Test
    public void testAddMensajeConExito() {
        Mensaje mensaje = new Mensaje(ID_MENSAJE,null, CONTENIDO_MENSAJE,  LocalDateTime.now(), null);
        contacto.addMensaje(mensaje);
        assertTrue(contacto.getMensajes().contains(mensaje));
        assertTrue(contacto.getMensajes().size() == 1);
    }

    @Test
    public void testDeleteMensajeConExito() {
        Mensaje mensaje = new Mensaje(ID_MENSAJE, null, CONTENIDO_MENSAJE, LocalDateTime.now(), null);
        contacto.addMensaje(mensaje);
        contacto.deleteMensaje(mensaje);

        assertFalse(contacto.getMensajes().contains(mensaje));
        assertTrue(contacto.getMensajes().isEmpty());
    }

    @Test
    public void testToStringConExito() {
        String toStringDeberiaMostrar = "Contacto [id=" + ID_CONTACTO + ", nombre=" + NOMBRE_CONTACTO + "]";

        assertEquals(toStringDeberiaMostrar, contacto.toString());
    }

    @Test
    public void testEqualsConContactoDuplicado() {
        Contacto contactoDuplicado = new Contacto(ID_CONTACTO, "Otro Nombre");
        assertEquals(contacto, contactoDuplicado);
    }

    @Test
    public void testEqualsConContactoIgual() {
        assertEquals(contacto, contacto);
    }

    @Test
    public void testEqualsConContactoNulo() {
        assertFalse(contacto.equals(null));
    }

    @SuppressWarnings("unlikely-arg-type")
	@Test
    public void testEqualsConClasesDistintas() {
        assertFalse(contacto.equals("objetoDistinto"));
    }
}
