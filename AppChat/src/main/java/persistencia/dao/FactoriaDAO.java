package persistencia.dao;

public abstract class FactoriaDAO {
	private static FactoriaDAO unicaInstancia;
	
	//Ubicación de la clase la factoria
	public static final String DAO_TDS = "persistencia.dao.TDSFactoriaDAO";

	/**
	 * Constructor vacío
	 */
	protected FactoriaDAO() {}
	
	/**
	 * 
	 * @param tipo Nombre de la clase (incluido el paquete) Ej: 'persistencia.dao.FactoriaDAO'
	 * @return Factoria única
	 * @throws DAOException
	 */
	@SuppressWarnings("deprecation")
	public static FactoriaDAO getInstancia(String tipo) throws DAOException{
		if (unicaInstancia == null) {
			try {
				unicaInstancia= (FactoriaDAO) Class.forName(tipo).newInstance();
			}catch (Exception e) {
				throw new DAOException(e.getMessage());
			}
		}
		return unicaInstancia;
	}
	
	
	
	public static FactoriaDAO getInstancia() throws DAOException {
		if(unicaInstancia == null)
			return getInstancia(DAO_TDS);//FAQ Se puede conseguir este valor de otra forma??
		else
			return unicaInstancia;
	}
	
	public abstract IAdaptadorUsuarioDAO getUsuarioDAO();
	public abstract IAdaptadorGrupoDAO getGrupoDAO();
	public abstract IAdaptadorContactoIndividualDAO getContactoIndividualDAO();
	public abstract IAdaptadorMensajeDAO getMensajeDAO();
	
}
