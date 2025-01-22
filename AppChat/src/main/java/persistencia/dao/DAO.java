package persistencia.dao;

import java.util.List;

public interface DAO<T> {
	
	/**
	 * Operaciones CRUD
	 * @throws DAOException 
	 */
	
	public int add(T elemento) throws DAOException;
		
	public void delete(T elemento) throws DAOException;
	
	public void update(T elemento) throws DAOException;	
			
	public T getById(int id) throws DAOException;
	
	/**
	 * Algunas operaciones que facilitaran la base de datos 
	 */
	
	public List<T> getAll();
}
