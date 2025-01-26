package persistencia.dao;

public class TDSFactoriaDAO extends FactoriaDAO {

	public TDSFactoriaDAO() {}
	
	@Override
	public IAdaptadorUsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioDAO.getUnicaInstancia();
	}

	@Override
	public IAdaptadorGrupoDAO getGrupoDAO() {
		return AdaptadorGrupoDAO.getUnicaInstancia();
	}

	@Override
	public IAdaptadorContactoIndividualDAO getContactoIndividualDAO() {
		return AdaptadorContactoIndividualDAO.getUnicaInstancia();
	}

	@Override
	public IAdaptadorMensajeDAO getMensajeDAO() {
		return AdaptadorMensajeDAO.getUnicaInstancia();
	}

}
