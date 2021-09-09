package net.ecology.framework.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import net.ecology.framework.entity.Repo;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.global.GlobeConstants;


public abstract class ServiceMarshall<T extends Repo, PK extends Serializable> extends ServiceCore <T, PK>{
	private static final long serialVersionUID = -1326030262778654331L;

	protected abstract IPersistence<T, PK> getPersistence();

	protected Page<T> performSearch(String keyword, Pageable pageable){
		return DUMMY_PAGEABLE;
	}

	protected Page<T> getPaginatedObjects(Integer page, Integer size){
    PageRequest pageRequest = PageRequest.of(page-1, size, Sort.Direction.ASC, "id");
    return getPersistence().findAll(pageRequest);
	}

	public Page<T> getList(Integer pageNumber) {
		return getPaginatedObjects(pageNumber, GlobeConstants.DEFAULT_PAGE_SIZE);
	}

	public Page<T> getList(Integer pageNumber, Integer size) {
		return getPaginatedObjects(pageNumber, size);
	}

	public T save(T entity) {
		return getPersistence().save(entity);
	}

	public List<T> saveObjects(List<T> objects) {
		return getPersistence().saveAll(objects);
	}

	public T create(T entity) {
		return getPersistence().save(entity);
	}

	public T get(PK id) {
		return getPersistence().findById(id).orElse(null);
	}

	public T getById(PK id) {
		return get(id);
	}

	public void delete(PK id) {
		getPersistence().deleteById(id);
	}

	public void delete(T entity) {
		getPersistence().delete(entity);
	}

	public void deleteAll() {
		getPersistence().deleteAll();
	}

	public T update(T entity) {
		return getPersistence().saveAndFlush(entity);
	}

	public Long count() {
		return getPersistence().count();
	}

	@Transactional(readOnly = true)
	public List<T> getAll() {
		List<T> results = new ArrayList<>();
		getPersistence().findAll().forEach(results::add);
		return results;
	}

	public T getObject(PK id) {
		return this.get(id);
	}
	
}