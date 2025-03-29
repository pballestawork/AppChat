package persistencia.dao;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PoolDAOTest {
    
    private PoolDAO poolDAO;

    @Before
    public void setUp() {
        poolDAO = PoolDAO.getUnicaInstancia(); // Obtener la instancia única
    }

    /**
     * Clase auxiliar para probar la modificación de objetos en el pool
     */
    private static class ObjetoPrueba {
        private String valor;

        public ObjetoPrueba(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String nuevoValor) {
            this.valor = nuevoValor;
        }
    }

    @Test
    public void testModificacionDeObjetoRecuperadoAfectaAlPool() {
        int id = 1;
        ObjetoPrueba objetoOriginal = new ObjetoPrueba("Estado inicial");

        // Agregar el objeto al PoolDAO
        poolDAO.addObjeto(id, objetoOriginal);

        // Recuperar el objeto del PoolDAO
        ObjetoPrueba objetoRecuperado = (ObjetoPrueba) poolDAO.getObjeto(id);

        // Modificar el objeto recuperado
        objetoRecuperado.setValor("Nuevo estado");

        // Verificar que el objeto en el pool también cambió
        ObjetoPrueba objetoDesdePool = (ObjetoPrueba) poolDAO.getObjeto(id);
        assertEquals("Nuevo estado", objetoDesdePool.getValor());

        // También podemos comprobar que la referencia sigue siendo la misma
        assertSame(objetoOriginal, objetoRecuperado);
        assertSame(objetoRecuperado, objetoDesdePool);
    }

    @Test
    public void testObjetoNoPresenteDevuelveNull() {
        // Intentar recuperar un objeto que no existe en la caché
        Object objetoRecuperado = poolDAO.getObjeto(999);

        // Debe devolver null porque el objeto no está en la caché
        assertNull("El objeto recuperado debería ser null", objetoRecuperado);
    }

    @Test
    public void testContieneObjeto() {
        int id = 2;
        ObjetoPrueba objeto = new ObjetoPrueba("Otro objeto");

        poolDAO.addObjeto(id, objeto);

        assertTrue("El PoolDAO debería contener el objeto", poolDAO.contiene(id));
    }
}
