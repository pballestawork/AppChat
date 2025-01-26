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
	 * 3. Crea la entidad
	 * 4. Añadir propiedades a la entidad
	 * 5. Se registra la entidad y se asocia su id con el objeto
	 * @param elemento
	 */
	public void add(T elemento);
		
	/**
	 * 1. Recuperar entidad
	 * 2. Eliminar agregados
	 * 3. Eliminar entidad
	 * @param elemento
	 */
	public void delete(T elemento);
	
	/**
	 * 1. Recuperar entidad
	 * 2. Recorrer propiedades moficicando el valor
	 * @param elemento
	 */
	public void update(T elemento);	
	
	// 1. Si está en el PoolDAO se retorna
	// 2. Recuperar la entidad, si no existe devolver null
	// 3. Inicializamos objeto con propiedades basicas y añadir al poolDAO 
		//3.1 Recuperamos propiedades que no son objetos!!!
		//3.2 Añadimos en el poolDAO para evitar bidireccionalidad infinita
	// 4. Recuperar referenciados y actualizar objeto
	// 5. Retornar objeto	
	/** 
	 * 1. Si está en el PoolDAO se retorna
	 * 2. Recuperar la entidad, si no existe devolver null
	 * 3. Inicializamos objeto con propiedades basicas y añadir al poolDAO 
	 * 4. Recuperar referenciados y actualizar objeto
	 * 5. Retornar objeto
	 * @param id
	 * @return
	 */
	public T getById(int id);
	
	/**
	 * Devuelve todos los elementos del tipo indicado 
 
	 */
	public List<T> getAll();
}
