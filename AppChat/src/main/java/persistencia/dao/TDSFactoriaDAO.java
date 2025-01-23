package persistencia.dao;

public class TDSFactoriaDAO extends FactoriaDAO {

	public TDSFactoriaDAO() {}
	
	@Override
	public IAdaptadorUsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioDAO.getUnicaInstancia();
	}

	@Override
	public IAdaptadorGrupoDAO getGrupoDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAdaptadorContactoIndividualDAO getContactoIndividualDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAdaptadorMensajeDAO getMensajeDAO() {
		// TODO Auto-generated method stub
		return null;
	}

}
