package persistencia.dao;

import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import utils.Utils;

public class AdaptadorGrupoDAO implements IAdaptadorGrupoDAO{
	private static ServicioPersistencia servicioPersistencia;
	private static AdaptadorGrupoDAO unicaInstancia = null;
	private PoolDAO poolDAO;
	private static IAdaptadorMensajeDAO adaptadorMensaje;
	private static IAdaptadorContactoIndividualDAO adaptadorContactoIndividual;

	public static final String TIPO_GRUPO = "Grupo";
	private final String PROP_NOMBRE = "nombre";
	private final String PROP_MENSAJES = "mensajes";
	private final String PROP_IMAGEN = "imagen";
	private final String PROP_MIEMBROS = "miembros";
	
	/**
	 * Constructor privado -> singleton
	 */
	private AdaptadorGrupoDAO() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		poolDAO = PoolDAO.getUnicaInstancia();
		
	}

	/**
	 * Metodo singleton, devuelve siempre la misma instancia
	 * 
	 * @return instancia de la clase
	 */
	public static AdaptadorGrupoDAO getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorGrupoDAO();
			//Inicializamos los adaptadores
			try {
				adaptadorMensaje = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS).getMensajeDAO();
				adaptadorContactoIndividual = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS).getContactoIndividualDAO();
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}

		return unicaInstancia;
	}
	
	@Override
	public void add(Grupo elemento) {
		// 1. Se comprueba que no esta registrada la entidad
		Entidad nuevaEntidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		if (nuevaEntidad != null)
			return;

		// 2. Se registran sus objetos referenciados
		elemento.getMensajes().stream().forEach(m -> adaptadorMensaje.add(m));
		elemento.getMiembros().stream().forEach(m -> adaptadorContactoIndividual.add(m));
		
		// 3. Crea la entidad
		nuevaEntidad = new Entidad();
		nuevaEntidad.setNombre(TIPO_GRUPO);
		// 4. Añadir propiedades a la entidad
		List<Propiedad> propiedadesLst = new LinkedList<Propiedad>();
		propiedadesLst.add(new Propiedad(PROP_NOMBRE, elemento.getNombre()));
		propiedadesLst.add(new Propiedad(PROP_IMAGEN, elemento.getImagen()));
		propiedadesLst.add(new Propiedad(PROP_MENSAJES, Utils.concatenarIds(elemento.getMensajes())));
		propiedadesLst.add(new Propiedad(PROP_MIEMBROS, Utils.concatenarIds(elemento.getMiembros())));
		nuevaEntidad.setPropiedades(propiedadesLst);

		// 5. Se registra la entidad y se asocia su id con el objeto
		nuevaEntidad = servicioPersistencia.registrarEntidad(nuevaEntidad);
		elemento.setId(nuevaEntidad.getId());
	}

	

	@Override
	public void delete(Grupo elemento) {
		Entidad entidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		
		for (Mensaje m : elemento.getMensajes())
			adaptadorMensaje.delete(m);
		
		servicioPersistencia.borrarEntidad(entidad);
		poolDAO.removeObject(elemento.getId());
	}

	@Override
	public void update(Grupo elemento) {
		Entidad entidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		if(entidad == null)
			return;
		
		for (Propiedad prop: entidad.getPropiedades()) {
			switch (prop.getNombre()) {
			case PROP_MENSAJES:
				prop.setValor(Utils.concatenarIds(elemento.getMensajes()));//TODO deberia ser actualizar mensajes?
				break;
			case PROP_NOMBRE:
				prop.setValor(elemento.getNombre());
				break;
			case PROP_MIEMBROS:
				prop.setValor(Utils.concatenarIds(elemento.getMiembros()));
				break;
			case PROP_IMAGEN:
				prop.setValor(elemento.getImagen());
				break;
			default:
				break;
			}
			servicioPersistencia.modificarPropiedad(prop);
		}
	}

	@Override
	public Grupo getById(int id) {
		// 1. Si está en el PoolDAO se retorna
		if (poolDAO.contiene(id))
			return (Grupo) poolDAO.getObjeto(id);

		// 2. Recuperar la entidad, si no existe devolver null
		Entidad e = servicioPersistencia.recuperarEntidad(id);

		if (e == null)
			return null;

		// 3. Inicializamos objeto con propiedades basicas y añadir al poolDAO 
		Grupo grupo = new Grupo();

		//3.1 Recuperamos propiedades que no son objetos!!!
		String nombre = servicioPersistencia.recuperarPropiedadEntidad(e, PROP_NOMBRE);
		String imagen = servicioPersistencia.recuperarPropiedadEntidad(e, PROP_IMAGEN);
		String idsMensajes = servicioPersistencia.recuperarPropiedadEntidad(e, PROP_MENSAJES);
		String idsMiembros = servicioPersistencia.recuperarPropiedadEntidad(e, PROP_MIEMBROS);
		
		grupo.setId(e.getId());
		grupo.setNombre(nombre);
		grupo.setImagen(imagen);
		
		//3.2 Añadimos en el poolDAO para evitar bidireccionalidad infinita
		poolDAO.addObjeto(e.getId(), grupo);
		
		// 4. Recuperar referenciados y actualizar objeto
		List<Mensaje> mensajes = obtenerMensajes(idsMensajes);
		List<ContactoIndividual> miembros = obtenerMiembros(idsMiembros);
		grupo.setMensajes(mensajes);
		grupo.setMiembros(miembros);
		
		// 5. Retornar objeto
		return grupo;
	}
	
	@Override
	public List<Grupo> getAll() {
		List<Grupo> grupos = new LinkedList<Grupo>();

		for (Entidad entidad : servicioPersistencia.recuperarEntidades(TIPO_GRUPO))
			grupos.add( getById(entidad.getId()) );

		return grupos;
	}
	
	//TODO Generalizar función
	private List<ContactoIndividual> obtenerMiembros(String idsLst) {
		List<Integer> idLst = Utils.getIdsByCadena(idsLst);
		List<ContactoIndividual> entidadLst = new LinkedList<ContactoIndividual>();
		for (Integer id : idLst) {
			entidadLst.add(adaptadorContactoIndividual.getById(id));// FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS).getMensajeDAO();
		}
		return entidadLst;
	}

	//TODO Generalizar función
	private List<Mensaje> obtenerMensajes(String idsLst) {
		List<Integer> idLst = Utils.getIdsByCadena(idsLst);
		List<Mensaje> entidadLst = new LinkedList<Mensaje>();
		for (Integer id : idLst) {
			entidadLst.add(adaptadorMensaje.getById(id));// FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS).getMensajeDAO();
		}
		return entidadLst;
	}
}
