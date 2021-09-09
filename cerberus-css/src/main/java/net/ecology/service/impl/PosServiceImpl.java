package net.ecology.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.entity.trade.Pos;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.repository.trade.PosPersistence;
import net.ecology.service.trade.PosService;

@Service
public class PosServiceImpl extends GenericService<Pos, Long> implements PosService{
	private static final long serialVersionUID = -8628938623043465764L;

	@Inject 
	private PosPersistence repository;
	
	protected IPersistence<Pos, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<Pos> getByName(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	public Optional<Pos> getByCode(String code) throws ObjectNotFoundException {
		return repository.findByCode(code);
	}

	@Override
	protected Page<Pos> performSearch(String keyword, Pageable pageable) {
		return repository.find(keyword, pageable);
	}

	@Override
	protected Optional<Pos> fetchBusinessObject(Object key) throws CerberusException {
		return super.getBizObject("findByName", key);
	}
}
