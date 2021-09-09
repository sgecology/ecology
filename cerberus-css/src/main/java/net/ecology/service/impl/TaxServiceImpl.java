package net.ecology.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.entity.trade.Tax;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.repository.trade.TaxPersistence;
import net.ecology.service.trade.TaxService;

@Service
public class TaxServiceImpl extends GenericService<Tax, Long> implements TaxService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1827391253714417691L;

	@Inject 
	private TaxPersistence repository;
	
	protected IPersistence<Tax, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<Tax> getByName(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	public Optional<Tax> getByCode(String code) throws ObjectNotFoundException {
		return repository.findByCode(code);
	}

	@Override
	protected Page<Tax> performSearch(String keyword, Pageable pageable) {
		return repository.find(keyword, pageable);
	}

	@Override
	protected Optional<Tax> fetchBusinessObject(Object key) throws CerberusException {
		return super.getBizObject("findByName", key);
	}
}
