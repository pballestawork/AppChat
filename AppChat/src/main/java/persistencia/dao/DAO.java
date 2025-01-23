package persistencia.dao;

import java.util.List;

/**
 * Operaciones CRUD
 * @throws DAOException 
 */
public interface DAO<T> {
	
	
	/**
	 * 1. Se comprueba que no esta registrada la entidad
	 * 2. Se registran sus objetos referenciados
	 * 3. Crea la entidad y asignamos id
	 * 4. Añadir propiedades a la entidad
	 * 5. Se registra la entidad y se asocia su id con el objeto
	 * 6. Se añade al poolDAO (opcional)
	 * @param elemento
	 * @return
	 * @throws DAOException
	 */
	public int add(T elemento) throws DAOException;
		
	/**
	 * 1. Recuperar entidad
	 * 2. Eliminar agregados
	 * 3. Eliminar entidad
	 * @param elemento
	 * @throws DAOException
	 */
	public void delete(T elemento) throws DAOException;
	
	
	/**
	 * 1. Recuperar entidad
	 * 2. Recorrer propiedades moficicando el valor
	 * @param elemento
	 * @throws DAOException
	 */
	public void update(T elemento) throws DAOException;	
		
	/** 
	 * 1. Si está en el PoolDAO se retorna
	 * 2. Recuperar la entidad, si no existe lanzar excepción
	 * 3. Inicializamos objeto con propiedades obtenidas y añadir al poolDAO 
	 * 4. Recuperar referenciados y actualizar objeto
	 * 5. Retornar objeto
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public T getById(int id) throws DAOException;
	
	/**
	 * Devuelve todos los elementos del tipo indicado 
	 * @throws DAOException 
	 */
	public List<T> getAll();
}
