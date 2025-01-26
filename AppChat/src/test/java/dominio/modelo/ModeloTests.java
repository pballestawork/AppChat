package dominio.modelo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ContactoIndividualTest.class, ContactoTest.class, GrupoTest.class, MensajeTest.class,
		UsuarioTest.class })
public class ModeloTests {

}
