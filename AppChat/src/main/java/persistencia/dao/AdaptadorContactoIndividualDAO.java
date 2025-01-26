package persistencia.dao;

import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import utils.Utils;

public class AdaptadorContactoIndividualDAO implements IAdaptadorContactoIndividualDAO{
	private static ServicioPersistencia servicioPersistencia;
	private static AdaptadorContactoIndividualDAO unicaInstancia = null;
	private PoolDAO poolDAO;
	private IAdaptadorMensajeDAO adaptadorMensajes;
	private IAdaptadorUsuarioDAO adaptadorUsuarioDAO;
	
	public static final String TIPO_CONTACTO_INDIVIDUAL = "ContactoIndividual";
	private final String PROP_NOMBRE = "nombre";
	private final String PROP_MENSAJES = "mensajes";
	private final String PROP_USUARIO = "usuario";
	
	/**
	 * Constructor privado -> singleton
	 */
	private AdaptadorContactoIndividualDAO() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		poolDAO = PoolDAO.getUnicaInstancia();
		try {
			adaptadorMensajes = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS).getMensajeDAO();
			adaptadorUsuarioDAO = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS).getUsuarioDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo singleton, devuelve siempre la misma instancia
	 * 
	 * @return instancia de la clase
	 */
	public static AdaptadorContactoIndividualDAO getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorContactoIndividualDAO();

		return unicaInstancia;
	}
	
	
	@Override
	public void add(ContactoIndividual elemento) {
		// 1. Se comprueba que no esta registrada la entidad
		Entidad nuevaEntidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		if (nuevaEntidad != null)
			return;//FAQ lanzar exc? throw new DAOException("El contacto " + elemento.getNombre() + " ya existe");

		// 2. registrar las entidades dependientes
		adaptadorUsuarioDAO.add(elemento.getUsuario());
		elemento.getMensajes().stream().forEach(m -> adaptadorMensajes.add(m));
		
		// 3. Crea la entidad
		nuevaEntidad = new Entidad();
		nuevaEntidad.setNombre(TIPO_CONTACTO_INDIVIDUAL);
		
		// 4. Añadir propiedades a la entidad
		List<Propiedad> propiedadesLst = new LinkedList<Propiedad>();
		propiedadesLst.add(new Propiedad(PROP_NOMBRE, elemento.getNombre()));
		propiedadesLst.add(new Propiedad(PROP_USUARIO, String.valueOf(elemento.getUsuario().getId())));
		propiedadesLst.add(new Propiedad(PROP_MENSAJES, Utils.concatenarIds(elemento.getMensajes())));
		nuevaEntidad.setPropiedades(propiedadesLst);

		// 5. Se registra la entidad y se asocia su id con el objeto
		nuevaEntidad = servicioPersistencia.registrarEntidad(nuevaEntidad);
		elemento.setId(nuevaEntidad.getId());
	}

	@Override
	public void delete(ContactoIndividual elemento) {
		Entidad entidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		
		for (Mensaje m : elemento.getMensajes())
			adaptadorMensajes.delete(m);
		
		servicioPersistencia.borrarEntidad(entidad);
		//TODO borrar del poolDAO
	}

	@Override
	public void update(ContactoIndividual elemento) {
		Entidad entidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		
		for (Propiedad prop: entidad.getPropiedades()) {
			switch (prop.getNombre()) {
			case PROP_MENSAJES:
				prop.setValor(Utils.concatenarIds(elemento.getMensajes()));//TODO deberia ser actualizar mensajes?
				break;
			case PROP_NOMBRE:
				prop.setValor(elemento.getNombre());
				break;
			case PROP_USUARIO:
				prop.setValor(String.valueOf(elemento.getUsuario().getId()));
				break;
			default:
				break;
			}
			servicioPersistencia.modificarPropiedad(prop);
		}
		
	}

	@Override
	public ContactoIndividual getById(int id) {
		// 1. Si está en el PoolDAO se retorna
		if (poolDAO.contiene(id))
			return (ContactoIndividual) poolDAO.getObjeto(id);
		
		// 2. Recuperar la entidad, si no existe devolver null
		Entidad entidad = servicioPersistencia.recuperarEntidad(id);
		if (entidad == null)
			return null;//FAQ lanzar exc? throw new DAOException("El contacto con " + id + " no existe");

		// 3. Inicializamos objeto con propiedades basicas y añadir al poolDAO 
		ContactoIndividual contacto = new ContactoIndividual();
		
			// 3.1 Recuperamos propiedades que no son objetos!!!
		String nombre = servicioPersistencia.recuperarPropiedadEntidad(entidad, PROP_NOMBRE);
		String idMensajeLst = servicioPersistencia.recuperarPropiedadEntidad(entidad, PROP_MENSAJES);
		String idUsuario = servicioPersistencia.recuperarPropiedadEntidad(entidad, PROP_USUARIO);
		contacto.setId(entidad.getId());
		contacto.setNombre(nombre);
		
			// 3.2 Añadimos en el poolDAO para evitar bidireccionalidad infinita
		poolDAO.addObjeto(entidad.getId(), contacto);
		
		// 4. Recuperar referenciados y actualizar objeto
		List<Mensaje> mensajes = obtenerMensajes(idMensajeLst);
		Usuario usuario = adaptadorUsuarioDAO.getById(Integer.parseInt(idUsuario));
		
		contacto.setMensajes(mensajes);
		contacto.setUsuario(usuario);
		
		// 5. Retornar objeto
		return contacto;
	}


	@Override
	public List<ContactoIndividual> getAll() {
		List<ContactoIndividual> contactos = new LinkedList<ContactoIndividual>();

		for (Entidad entidad : servicioPersistencia.recuperarEntidades(TIPO_CONTACTO_INDIVIDUAL))
			contactos.add(getById(entidad.getId()));

		return contactos;
	}
	
	//TODO Generalizar función
	private List<Mensaje> obtenerMensajes(String idsLst) {
		List<Integer> idLst = Utils.getIdsByCadena(idsLst);
		List<Mensaje> entidadLst = new LinkedList<Mensaje>();
		for (Integer id : idLst) {
			entidadLst.add(adaptadorMensajes.getById(id));// FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS).getMensajeDAO();
		}
		return entidadLst;
	}
	
}
