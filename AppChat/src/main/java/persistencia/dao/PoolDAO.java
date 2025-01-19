package persistencia.dao;

import java.util.Hashtable;
/**
 * PoolDAO funciona como una lazy cache.
 * Los objetos que maneja la aplicación se encuentran en memoria (en este PoolDAO).
 * Cuando un objeto esté persistido y no se encuentre en esta cache
 * se recuperará de la base de datos y se añadirá al PoolDAO (la cache)
 * Cuando crea cree un nuevo objeto se añadirá tanto a la cache como
 * a la base de datos.
 */
public class PoolDAO {
	private static PoolDAO unicaInstancia;
	private Hashtable<Integer, Object> pool;

	private PoolDAO() {
		pool = new Hashtable<Integer, Object>();
	}

	public static PoolDAO getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new PoolDAO();
		return unicaInstancia;

	}

	/**
	 * @param id
	 * @return devuelve null si no lo encuntra
	 */
	public Object getObjeto(int id) {
		return pool.get(id);
	}

	public void addObjeto(int id, Object objeto) {
		pool.put(id, objeto);
	}

	public boolean contiene(int id) {
		return pool.containsKey(id);
	}
}
