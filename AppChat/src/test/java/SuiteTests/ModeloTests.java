package SuiteTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import dominio.modelo.ContactoIndividualTest;
import dominio.modelo.ContactoTest;
import dominio.modelo.GrupoTest;
import dominio.modelo.MensajeTest;
import dominio.modelo.UsuarioTest;
import persistencia.dao.AdaptadorContactoIndividualDAOTest;
import persistencia.dao.AdaptadorGrupoDAOTest;
import persistencia.dao.AdaptadorMensajeDAOTest;
import persistencia.dao.AdaptadorUsuarioDAOTest;

@RunWith(Suite.class)
@SuiteClasses({ ContactoIndividualTest.class, ContactoTest.class, GrupoTest.class, MensajeTest.class, UsuarioTest.class,
		AdaptadorContactoIndividualDAOTest.class, AdaptadorGrupoDAOTest.class, AdaptadorUsuarioDAOTest.class,
		AdaptadorMensajeDAOTest.class })
public class ModeloTests {

}
