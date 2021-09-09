package net.ecology.framework.service;

import java.io.Serializable;
import java.util.List;

import net.ecology.framework.entity.Repo;

public interface IService<T extends Repo, K extends Serializable> extends Serializable {
  /**
   * Get object with the provided key.
   * 
   * @param id The object key
   * @return The Object
   */
	T getObject(K id);

  /**
   * Search objects with the provided search parameter.
   * 
   * @param searchParam The search key or parameter
   * @return The list of found objects
   */
	List<T> search(Object searchParam);

	IService<T, K> saveAndFlush(T entity);
}