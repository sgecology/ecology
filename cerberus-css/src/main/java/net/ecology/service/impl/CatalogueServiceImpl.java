package net.ecology.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.css.persistence.general.CataloguePersistence;
import net.ecology.css.specification.CatalogueSpecification;
import net.ecology.entity.general.Catalogue;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.service.general.CatalogueService;

@Service
public class CatalogueServiceImpl extends GenericService<Catalogue, Long> implements CatalogueService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1435351574637430464L;
	@Inject 
	private CataloguePersistence repository;
	
	protected IPersistence<Catalogue, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<Catalogue> getByName(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	public Optional<Catalogue> getByCode(String code) throws ObjectNotFoundException {
		return repository.findByCode(code);
	}

	@Override
	protected Page<Catalogue> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Catalogue> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(CatalogueSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}

	@Override
	protected Optional<Catalogue> fetchBusinessObject(Object key) throws CerberusException {
		return super.getBizObject("findByName", key);
	}
}
