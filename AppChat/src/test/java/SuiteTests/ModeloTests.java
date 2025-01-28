package SuiteTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import dominio.modelo.ContactoIndividualTest;
import dominio.modelo.ContactoTest;
import dominio.modelo.GrupoTest;
import dominio.modelo.MensajeTest;
import dominio.modelo.UsuarioTest;

@RunWith(Suite.class)
@SuiteClasses({ ContactoIndividualTest.class, ContactoTest.class, GrupoTest.class, MensajeTest.class,
		UsuarioTest.class })
public class ModeloTests {

}
