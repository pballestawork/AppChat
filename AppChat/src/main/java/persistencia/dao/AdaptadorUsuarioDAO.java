package persistencia.dao;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import dominio.modelo.Contacto;
import dominio.modelo.ContactoIndividual;
import dominio.modelo.Grupo;
import dominio.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import utils.Utils;

public class AdaptadorUsuarioDAO implements IAdaptadorUsuarioDAO {
	private static ServicioPersistencia servicioPersistencia;
	private static PoolDAO poolDAO;
	private static AdaptadorUsuarioDAO unicaInstancia = null;
	private static final String PROP_NOMBRE = "nombre";
	private static final String PROP_TELEFONO = "telefono";
	private static final String PROP_EMAIL = "email";
	private static final String PROP_CONTRASENA = "contrasena";
	private static final String PROP_FOTO_PERFIL = "fotoPerfil";
	private static final String PROP_ES_PREMIUM = "esPremium";
	private static final String PROP_CONTACTOS = "contactos";
	public static final String TIPO_USUARIO = "Usuario";

	/**
	 * Constructor privado -> singleton
	 */
	private AdaptadorUsuarioDAO() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		poolDAO = PoolDAO.getUnicaInstancia();
	}

	/**
	 * Metodo singleton, devuelve siempre la misma instancia
	 * 
	 * @return instancia de la clase
	 */
	public static AdaptadorUsuarioDAO getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new AdaptadorUsuarioDAO();

		return unicaInstancia;
	}

	@Override
	public int add(Usuario elemento) throws DAOException {
		//1. Se comprueba que no esta registrada la entidad
		Entidad nuevaEntidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		if (nuevaEntidad != null)
			throw new DAOException("El usuario " + elemento.getNombre() + " ya existe");

		// TODO 2. registrar los contactos en ContactoDAO
		String contactosId = registrarMasObtenerContactosId(elemento.getContactos());
		
		
		
		// 3. Crea la entidad
		nuevaEntidad = new Entidad();
		nuevaEntidad.setNombre(TIPO_USUARIO);
		// 4. Añadir propiedades a la entidad
		List<Propiedad> propiedadesLst = new LinkedList<Propiedad>();
		propiedadesLst.add(new Propiedad(PROP_NOMBRE, elemento.getNombre()));
		propiedadesLst.add(new Propiedad(PROP_TELEFONO, elemento.getTelefono()));
		propiedadesLst.add(new Propiedad(PROP_EMAIL, elemento.getEmail()));
		propiedadesLst.add(new Propiedad(PROP_CONTRASENA, elemento.getContrasena()));
		propiedadesLst.add(new Propiedad(PROP_FOTO_PERFIL, elemento.getFotoPerfil()));
		propiedadesLst.add(new Propiedad(PROP_ES_PREMIUM, elemento.isEsPremium() ? "true" : "false"));// FAQ reconoce el booleano?
		propiedadesLst.add(new Propiedad(PROP_CONTACTOS, contactosId));
		nuevaEntidad.setPropiedades(propiedadesLst);

		// 5. Se registra la entidad y se asocia su id con el objeto
		nuevaEntidad = servicioPersistencia.registrarEntidad(nuevaEntidad);
		elemento.setId(nuevaEntidad.getId());

		// 6. Se añade al poolDAO (opcional)
		poolDAO.addObjeto(elemento.getId(), elemento);// FAQ El poolDAO debería almacenar todos los usuarios?

		return elemento.getId();
	}

	@Override
	public void delete(Usuario elemento) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Usuario elemento) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Usuario getById(int id) throws DAOException {

		if (poolDAO.contiene(id))
			return (Usuario) poolDAO.getObjeto(id);

		Entidad e = servicioPersistencia.recuperarEntidad(id);

		if (e == null)
			throw new DAOException("El usuario con " + id + " no existe");

		Usuario usuario = new Usuario();

		// Recuperamos propiedades que no son objetos!!!
		usuario.setId(e.getId());
		usuario.setNombre(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_NOMBRE));
		usuario.setTelefono(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_TELEFONO));
		usuario.setEmail(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_EMAIL));
		usuario.setContrasena(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_CONTRASENA));
		usuario.setFotoPerfil(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_FOTO_PERFIL));
		usuario.setEsPremium(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_ES_PREMIUM) == "true");

		poolDAO.addObjeto(e.getId(), usuario);
		
		List<Contacto> contactos = obtenerContactos(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_CONTACTOS));
		usuario.setContactos(contactos);
		
		return usuario;
	}

	@Override
	public List<Usuario> getAll() {
		List<Usuario> usuarios = new LinkedList<Usuario>();

		for (Entidad entidad : servicioPersistencia.recuperarEntidades(TIPO_USUARIO)) {
			try {
				usuarios.add(getById(entidad.getId()));
			} catch (DAOException e) {
			} // No tiene sentido que no existan usuarios que acabamos de recuperar
		}

		return usuarios;
	}

	/**
	 * Registran los contactos asociados y devuelve los ids
	 * @param contactos
	 * @throws DAOException
	 */
	private String registrarMasObtenerContactosId(List<Contacto> contactos) throws DAOException {
		IAdaptadorContactoIndividualDAO adapatadorContactoDAO = TDSFactoriaDAO.getInstancia().getContactoIndividualDAO();
		IAdaptadorGrupoDAO adapatadorGrupoDAO = TDSFactoriaDAO.getInstancia().getGrupoDAO();
		for (Contacto contacto: contactos) {
			if (contacto instanceof ContactoIndividual) {
                ContactoIndividual individual = (ContactoIndividual) contacto;
				adapatadorContactoDAO.add(individual);
            } else if (contacto instanceof Grupo) {
                Grupo grupo = (Grupo) contacto;
                adapatadorGrupoDAO.add(grupo);
            }
		}
		
		//Concatenamos los id
		return Utils.concatenarIds(contactos);
	}
	
	/**
	 * Obtiene la lista de contactos a partir de una cadana parseada con los id de
	 * cada contacto.
	 * 
	 * @param recuperarPropiedadEntidad Ej "10,20,33,46"
	 * @return lista de contactos asociados a los id
	 * @throws DAOException 
	 */
	private List<Contacto> obtenerContactos(String contactosIds) throws DAOException {
		List<Integer> ids = Utils.getIdsByCadena(contactosIds);
		/*TODO recuperar los contactos (contactoIndividual+Grupo) utilizando una lista de Ids.
		 * NOTA: utilizando Id no distingues entre tipos de contacto entonces no se puede
		 * utilizar el contactoIndividualDAO o GrupoDAO. 
		 * NOTA 2: Es importante utilizar el DAO para evitar recursividad infinita por
		 * bidireccionalidad 
		 * NOTA 3: Como solución temporal se hará directamente sobre el servicioPersitencia
		 */
		List<Entidad> entidadesContacto = new LinkedList<Entidad>();
		List<Contacto> contactos = new LinkedList<Contacto>();
		
		ids.stream().forEach(id -> entidadesContacto.add(servicioPersistencia.recuperarEntidad(id)));
		
		IAdaptadorContactoIndividualDAO adaptadorContacto = TDSFactoriaDAO.getInstancia().getContactoIndividualDAO();
		IAdaptadorGrupoDAO adaptadorGrupo= TDSFactoriaDAO.getInstancia().getGrupoDAO();
		
		for (Entidad entidad : entidadesContacto) {
			if(entidad.getNombre()== AdaptadorContactoIndividualDAO.TIPO_CONTACTO_INDIVIDUAL) {
				Contacto c = adaptadorContacto.getById(entidad.getId());
				contactos.add(c);
			}else if(entidad.getNombre()== AdaptadorGrupoDAO.TIPO_GRUPO) {
				Contacto c = adaptadorGrupo.getById(entidad.getId());
				contactos.add(c);
			}
		}
		
		return contactos;
	}

}
