package net.ecology.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.entity.general.TaxGroup;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.repository.trade.TaxGroupPersistence;
import net.ecology.service.trade.TaxGroupService;

@Service
public class TaxGroupServiceImpl extends GenericService<TaxGroup, Long> implements TaxGroupService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6265119761602842252L;
	@Inject 
	private TaxGroupPersistence repository;
	
	protected IPersistence<TaxGroup, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<TaxGroup> getByName(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	protected Optional<TaxGroup> fetchBusinessObject(Object key) throws CerberusException {
		return super.getBizObject("findByName", key);
	}
}
