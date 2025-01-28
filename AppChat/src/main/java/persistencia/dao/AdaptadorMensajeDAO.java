package persistencia.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import dominio.modelo.Mensaje;
import dominio.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorMensajeDAO implements IAdaptadorMensajeDAO{
	private static ServicioPersistencia servicioPersistencia;
	private static AdaptadorMensajeDAO unicaInstancia = null;
	private static IAdaptadorUsuarioDAO adaptadorUsuarioDAO;
	private static DateTimeFormatter formatter; 
	private final String PROP_EMISOR = "emisor";
	private final String PROP_CONTENIDO = "contenido";
	private final String PROP_FECHA_ENVIO = "fechaEnvio";
	public final String TIPO_MENSAJE = "Mensaje";
	
	/**
	 * Constructor privado -> singleton
	 */
	private AdaptadorMensajeDAO() {
		servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * Metodo singleton, devuelve siempre la misma instancia
	 * 
	 * @return instancia de la clase
	 */
	public static AdaptadorMensajeDAO getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorMensajeDAO();
			try {
				adaptadorUsuarioDAO = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS).getUsuarioDAO();
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}

		return unicaInstancia;
	}
	
	@Override
	public void add(Mensaje elemento) {
		// 1. Se comprueba que no esta registrada la entidad
		
		Entidad nuevaEntidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		if (nuevaEntidad != null)
			return;//FAQ throw new DAOException("El mensaje " + elemento.getId() + " ya existe");
		
		// 2. registrar las entidades dependientes
		adaptadorUsuarioDAO.add(elemento.getEmisor());//Seguramente ya exista
		
		// 3. Crea la entidad
		nuevaEntidad = new Entidad();
		nuevaEntidad.setNombre(TIPO_MENSAJE);
		
		// 4. Añadir propiedades a la entidad
		List<Propiedad> propiedadesLst = new LinkedList<Propiedad>();
		propiedadesLst.add(new Propiedad(PROP_EMISOR, String.valueOf(elemento.getEmisor().getId())));
		propiedadesLst.add(new Propiedad(PROP_CONTENIDO, elemento.getContenido()));
		propiedadesLst.add(new Propiedad(PROP_FECHA_ENVIO, elemento.getFechaEnvio().format(formatter)));
		nuevaEntidad.setPropiedades(propiedadesLst);
		
		// 5. Se registra la entidad y se asocia su id con el objeto
		nuevaEntidad = servicioPersistencia.registrarEntidad(nuevaEntidad);
		elemento.setId(nuevaEntidad.getId());
	}

	@Override
	public void delete(Mensaje elemento) {
		Entidad entidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		servicioPersistencia.borrarEntidad(entidad);
		//TODO borrar del poolDAO??		
	}

	@Override
	public void update(Mensaje elemento) {
		Entidad entidad = servicioPersistencia.recuperarEntidad(elemento.getId());
		
		for (Propiedad prop: entidad.getPropiedades()) {
			switch (prop.getNombre()) {
			case PROP_CONTENIDO:
				prop.setValor(elemento.getContenido());//TODO esto puede meter petardazo
				break;
			case PROP_EMISOR:
				prop.setValor(String.valueOf(elemento.getEmisor().getId()));
				break;
			case PROP_FECHA_ENVIO:
				prop.setValor(elemento.getFechaEnvio().format(formatter));
				break;
			default:
				break;
			}
			servicioPersistencia.modificarPropiedad(prop);
		}
		
	}

	@Override
	public Mensaje getById(int id) {
		// 1. Si está en el PoolDAO se retorna (FAQ Debería entrar en el poolDAO?)
		// 2. Recuperar la entidad, si no existe devolver null
		Entidad e = servicioPersistencia.recuperarEntidad(id);
		
		for (Propiedad i: e.getPropiedades()) {
				System.out.println(i.getValor());
		}
		
		
		if (e == null)
			return null;//FAQ lanzar excepción? throw new DAOException("El mensaje con id " + id + " no existe");

		// 	3. Inicializamos objeto con propiedades basicas y añadir al poolDAO 
			//3.1 Recuperamos propiedades que no son objetos!!!
		String contenido = servicioPersistencia.recuperarPropiedadEntidad(e, PROP_CONTENIDO);
		String fechaEnvio = servicioPersistencia.recuperarPropiedadEntidad(e, PROP_FECHA_ENVIO);
		String idEmisor = servicioPersistencia.recuperarPropiedadEntidad(e, PROP_EMISOR);
		System.out.println("GetById " + "fechaEnvio sin parsear: " + fechaEnvio);
		System.out.println("GetById " + "fechaEnvio parseada: " + LocalDateTime.parse(fechaEnvio, formatter));
		LocalDateTime fechaEnvioParseada = LocalDateTime.parse(fechaEnvio, formatter);
			//3.2 Añadimos en el poolDAO para evitar bidireccionalidad infinita (FAQ debería??)
		
		// 4. Recuperar referenciados y actualizar objeto
		Usuario emisor = adaptadorUsuarioDAO.getById(Integer.parseInt(idEmisor));
		Mensaje mensaje = new Mensaje(e.getId(),emisor,contenido,fechaEnvioParseada,true);//TODO tratar el tipo
		
		// 5. Retornar objeto
		return mensaje;
	}

	@Override
	public List<Mensaje> getAll() {
		List<Mensaje> mensajes = new LinkedList<Mensaje>();

		for (Entidad entidad : servicioPersistencia.recuperarEntidades(TIPO_MENSAJE))
			mensajes.add(getById(entidad.getId()));


		return mensajes;
	}

}
