package persistencia.dao;

import org.junit.Before;
import org.junit.BeforeClass;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorContactoIndividualDAOTest {
	private static ServicioPersistencia servicioPersistencia;
	private static IAdaptadorContactoIndividualDAO adaptadorContactoIndividualDAO;
	
	
	@BeforeClass
	public static void setup() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		try {
			FactoriaDAO factoriaDAO = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorContactoIndividualDAO = factoriaDAO.getContactoIndividualDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public static void beforeEach() {
		
	}
}
