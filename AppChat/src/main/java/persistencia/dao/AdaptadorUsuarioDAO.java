package persistencia.dao;

import java.time.LocalDate;
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
	private static AdaptadorUsuarioDAO unicaInstancia = null;
	private PoolDAO poolDAO;
	private static IAdaptadorContactoIndividualDAO adaptadorContactoIndividual;
	private static IAdaptadorGrupoDAO adaptadorGrupo;
	
	public static final String TIPO_USUARIO = "Usuario";
	private final String PROP_NOMBRE = "nombre";
	private final String PROP_TELEFONO = "telefono";
	private final String PROP_EMAIL = "email";
	private final String PROP_CONTRASENA = "contrasena";
	private final String PROP_FOTO_PERFIL = "fotoPerfil";
	private final String PROP_ES_PREMIUM = "esPremium";
	private final String PROP_SALUDO = "saludo";
	private final String PROP_FECHA_NACIMIENTO = "fechaNacimiento";
	private final String PROP_CONTACTOS = "contactos";

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
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorUsuarioDAO();
			//Inicializamos los adaptadores
			try {
				adaptadorContactoIndividual = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS).getContactoIndividualDAO();
				adaptadorGrupo = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS).getGrupoDAO();
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}

		return unicaInstancia;
	}

	@Override
	public void add(Usuario elemento) {
		//1. Se comprueba que no esta registrada la entidad
		Entidad nuevaEntidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		if (nuevaEntidad != null)
			return;

		// 2. Se registran sus objetos referenciados
		registrarContactos(elemento.getContactos());
		
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
		propiedadesLst.add(new Propiedad(PROP_SALUDO, elemento.getSaludo()));
		propiedadesLst.add(new Propiedad(PROP_CONTACTOS, Utils.concatenarIds(elemento.getContactos())));
		propiedadesLst.add(new Propiedad(PROP_FECHA_NACIMIENTO, elemento.getFechaNacimiento().toString()));

		nuevaEntidad.setPropiedades(propiedadesLst);

		// 5. Se registra la entidad y se asocia su id con el objeto
		nuevaEntidad = servicioPersistencia.registrarEntidad(nuevaEntidad);
		elemento.setId(nuevaEntidad.getId());
	}
	
	@Override
	public void delete(Usuario elemento) {
		Entidad entidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		
		if(entidad == null)
			return;
		
		for (Contacto c: elemento.getContactos()) {
			switch (entidad.getNombre()) {
            case AdaptadorContactoIndividualDAO.TIPO_CONTACTO_INDIVIDUAL:
                adaptadorContactoIndividual.delete((ContactoIndividual) c);
                break;
            case AdaptadorGrupoDAO.TIPO_GRUPO:
                adaptadorGrupo.delete((Grupo) c);
                break;
            default:
			}	
		} 
		
		servicioPersistencia.borrarEntidad(entidad);
		poolDAO.removeObject(elemento.getId());
	}

	@Override
	public void update(Usuario elemento) {
		Entidad entidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		if (entidad == null)
			return;
		
		for (Propiedad prop: entidad.getPropiedades()) {
			switch (prop.getNombre()) {
			case PROP_CONTACTOS:
				prop.setValor(Utils.concatenarIds(elemento.getContactos()));
				break;
			case PROP_CONTRASENA:
				prop.setValor(elemento.getContrasena());
				break;
			case PROP_EMAIL:
				prop.setValor(elemento.getEmail());
				break;
			case PROP_ES_PREMIUM:
				prop.setValor(elemento.isEsPremium() ? "true" : "false");
				break;
			case PROP_FOTO_PERFIL:
				prop.setValor(elemento.getFotoPerfil());
				break;
			case PROP_NOMBRE:
				prop.setValor(elemento.getNombre());
				break;
			case PROP_TELEFONO:
				prop.setValor(elemento.getTelefono());
				break;
			case PROP_SALUDO:
				prop.setValor(elemento.getSaludo());
				break;
			case PROP_FECHA_NACIMIENTO:
				prop.setValor(elemento.getFechaNacimiento().toString());
				break;
			default:
				break;
			}
			servicioPersistencia.modificarPropiedad(prop);
		}
	}

	@Override
	public Usuario getById(int id) {
		// 1. Si está en el PoolDAO se retorna
		if (poolDAO.contiene(id))
			return (Usuario) poolDAO.getObjeto(id);

		// 2. Recuperar la entidad, si no existe devolver null
		Entidad e = servicioPersistencia.recuperarEntidad(id);
		
		if (e == null)
			return null;
		
		// 3. Inicializamos objeto con propiedades basicas y añadir al poolDAO
		Usuario usuario = new Usuario();

			// 3.1 Recuperamos propiedades que no son objetos!!!
		usuario.setId(e.getId());
		usuario.setNombre(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_NOMBRE));
		usuario.setTelefono(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_TELEFONO));
		usuario.setEmail(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_EMAIL));
		usuario.setContrasena(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_CONTRASENA));
		usuario.setFotoPerfil(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_FOTO_PERFIL));
		usuario.setSaludo(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_SALUDO));
		usuario.setEsPremium(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_ES_PREMIUM).equals("true"));
		
		String fecha = servicioPersistencia.recuperarPropiedadEntidad(e, PROP_FECHA_NACIMIENTO);
		usuario.setFechaNacimiento(LocalDate.parse(fecha));

			// 3.2 Añadimos en el poolDAO para evitar bidireccionalidad infinita
		poolDAO.addObjeto(e.getId(), usuario);
		
		// 4. Recuperar referenciados y actualizar objeto
		List<Contacto> contactos = obtenerContactos(servicioPersistencia.recuperarPropiedadEntidad(e, PROP_CONTACTOS));
		usuario.setContactos(contactos);
		
		// 5. Retornar objeto
		return usuario;
	}

	@Override
	public List<Usuario> getAll() {
		List<Usuario> usuarios = new LinkedList<Usuario>();

		for (Entidad entidad : servicioPersistencia.recuperarEntidades(TIPO_USUARIO))
			usuarios.add(getById(entidad.getId()));

		return usuarios;
	}

	/**
	 * Registran los contactos asociados y devuelve los ids
	 * @param contactos
	 * @throws DAOException
	 */
	private void registrarContactos(List<Contacto> contactos) {
		for (Contacto contacto: contactos) {
			if (contacto instanceof ContactoIndividual) {
                ContactoIndividual individual = (ContactoIndividual) contacto;
				adaptadorContactoIndividual.add(individual);
            } else if (contacto instanceof Grupo) {
                Grupo grupo = (Grupo) contacto;
                adaptadorGrupo.add(grupo);
            }
		}
	}
	
	/*TODO recuperar los contactos (contactoIndividual+Grupo) utilizando una lista de Ids.
	 * NOTA: utilizando Id no distingues entre tipos de contacto entonces no se puede
	 * utilizar el contactoIndividualDAO o GrupoDAO. 
	 * NOTA 2: Es importante utilizar el DAO para evitar recursividad infinita por
	 * bidireccionalidad 
	 * NOTA 3: Como solución temporal se hará directamente sobre el servicioPersitencia
	 */
	/**
	 * Obtiene la lista de contactos a partir de una cadana parseada con los id de
	 * cada contacto.
	 * 
	 * @param recuperarPropiedadEntidad Ej "10,20,33,46"
	 * @return lista de contactos asociados a los id
	 * @throws DAOException 
	 */	
	private List<Contacto> obtenerContactos(String contactosIds) {
	    // Lista de Ids
	    List<Integer> ids = Utils.getIdsByCadena(contactosIds);

	    // Recuperar las entidades
	    List<Entidad> entidadesContacto = ids.stream()
	            .map(id -> servicioPersistencia.recuperarEntidad(id))
	            .collect(Collectors.toList());

	    // Segun la entidad, generar la lista de contactos
	    List<Contacto> contactos = new LinkedList<>();
	    for (Entidad entidad : entidadesContacto) {
	        switch (entidad.getNombre()) {
	            case AdaptadorContactoIndividualDAO.TIPO_CONTACTO_INDIVIDUAL:
	                contactos.add(adaptadorContactoIndividual.getById(entidad.getId()));
	                break;
	            case AdaptadorGrupoDAO.TIPO_GRUPO:
	                contactos.add(adaptadorGrupo.getById(entidad.getId()));
	                break;
	            default:
	        }
	    }

	    return contactos;
	}

}
