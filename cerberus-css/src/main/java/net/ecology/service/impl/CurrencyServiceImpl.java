package net.ecology.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.css.persistence.general.CurrencyPersistence;
import net.ecology.css.service.general.CurrencyService;
import net.ecology.css.service.system.SequenceManager;
import net.ecology.css.specification.CurrencySpecification;
import net.ecology.entity.general.Currency;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.global.GlobalConstants;

@Service
public class CurrencyServiceImpl extends GenericService<Currency, Long> implements CurrencyService{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3677680060876281817L;

	@Inject 
	private CurrencyPersistence repository;

	@Inject
	private SequenceManager sequenceManager;

	protected IPersistence<Currency, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<Currency> getByCode(String alphabeticCode) throws ObjectNotFoundException {
		return repository.findByCode(alphabeticCode);
	}

	@Override
	protected Page<Currency> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Currency> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(CurrencySpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}

	@Override
	public String nextSerial(String prefix) throws CerberusException {
		String newSerialNo = this.sequenceManager.getNewNumber(prefix, Integer.valueOf(GlobalConstants.SIZE_CODE));
		newSerialNo = prefix + newSerialNo.substring(prefix.length());
		return newSerialNo;
	}
}
