package persistencia.dao;

import java.util.List;

public interface DAO<T> {
	
	/**
	 * Operaciones CRUD
	 */
	
	public int add(T elemento);
		
	public void delete(T elemento);
	
	public void update(T elemento);	
			
	public T getById(int id);
	
	/**
	 * Algunas operaciones que facilitaran la base de datos 
	 */
	
	public List<T> getAll();
}
