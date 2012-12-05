package be.bonamis.umpire.dao;

import java.io.Serializable;
import java.util.List;



/**
 * Dao de base à toutes les entities.
 *
 * @param <E> Le type de l'entity à gérer
 * @param <P> Le type de la primary key de l'entity
 */
public interface Dao<T,P extends Serializable> {

	public class FindResult<T> {
		private final List<T> results;
		private final int totalRows;		
		
		public FindResult(int totalRows, List<T> results) {
			this.totalRows = totalRows;
			this.results = results;
		}
		
		public int getTotalRows() {
			return totalRows;
		}
		
		public List<T> getResults() {
			return results;
		}
	}
	
	T find(Integer id);
	
	List<T> findAll(); 
		
	void save(T entity);

	T merge(T entity);

	void delete(T entity);

	void flush();

	void clear();
	
	void refresh(T entity);
	
	void evict(T entity);
	

}
