package net.ecology.framework.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.ecology.domain.Context;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ContextExecutionException;
import net.ecology.framework.entity.Repo;
import net.ecology.framework.model.SearchParameter;

public interface IGenericService<T extends Repo, K extends Serializable> extends IService<T, K> {
	T save(T entity);
	T saveOrUpdate(T entity);
	List<T> saveObjects(List<T> objects);

	void remove(K id);
	void remove(T entity);
	void removeAll();

	boolean exists(String property, Object value);
	boolean exists(String property, String processingClause, Object ...values);

	Long count();
	Long count(String countByProperty, Object value);
	Long count(String countMethodName, Map<?, ?> parameters);

	String nextSerial(String prefix) throws CerberusException;

	Optional<T> getBusinessObject(Object key) throws CerberusException;
	List<T> getObjects();

	List<T> getVisibleObjects();
	/**
	 * Get objects with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable objects
	 */
	Page<T> getObjects(SearchParameter searchParameter);

	//Page<T> getObjects(Integer pageNumber);
	//Page<T> getObjects(Integer pageNumber, Integer size);
	Page<T> searchObjects(String keyword, Pageable pageable);
	Page<T> search(Map<String, Object> parameters);
	//Page<T> search(String keyword);
	Page<T> search(String keyword, Pageable pageable);

	List<T> imports(Map<Object, Object> parameters);
	List<T> search(String keyword);
	Context load(Context executionContext) throws ContextExecutionException;
}