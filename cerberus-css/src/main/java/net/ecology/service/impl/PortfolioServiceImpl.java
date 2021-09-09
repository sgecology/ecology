package net.ecology.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.entity.trade.Portfolio;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.repository.trade.PortfolioPersistence;
import net.ecology.service.trade.PortfolioService;

@Service
public class PortfolioServiceImpl extends GenericService<Portfolio, Long> implements PortfolioService{
	private static final long serialVersionUID = -8628938623043465764L;

	@Inject 
	private PortfolioPersistence repository;
	
	protected IPersistence<Portfolio, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<Portfolio> getByName(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	public Optional<Portfolio> getByCode(String code) throws ObjectNotFoundException {
		return repository.findByCode(code);
	}

	@Override
	protected Page<Portfolio> performSearch(String keyword, Pageable pageable) {
		return repository.find(keyword, pageable);
	}

	@Override
	protected Optional<Portfolio> fetchBusinessObject(Object key) throws CerberusException {
		return super.getBizObject("findByName", key);
	}
}
