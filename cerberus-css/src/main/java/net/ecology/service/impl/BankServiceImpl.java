package net.ecology.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.entity.trade.Bank;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.repository.trade.BankPersistence;
import net.ecology.service.trade.BankService;

@Service
public class BankServiceImpl extends GenericService<Bank, Long> implements BankService{
	private static final long serialVersionUID = -3460325091527495246L;

	@Inject 
	private BankPersistence repository;
	
	protected IPersistence<Bank, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<Bank> getByName(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	public Optional<Bank> getByCode(String code) throws ObjectNotFoundException {
		return repository.findByCode(code);
	}

	@Override
	protected Page<Bank> performSearch(String keyword, Pageable pageable) {
		return repository.find(keyword, pageable);
	}

	@Override
	protected Optional<Bank> fetchBusinessObject(Object key) throws CerberusException {
		return super.getBizObject("findByName", key);
	}
}
