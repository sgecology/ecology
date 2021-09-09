package net.ecology.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.entity.trade.BankBranch;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.repository.trade.BankBranchPersistence;
import net.ecology.service.trade.BankBranchService;

@Service
public class BankBranchServiceImpl extends GenericService<BankBranch, Long> implements BankBranchService{
	private static final long serialVersionUID = 1946792625414253696L;

	@Inject 
	private BankBranchPersistence repository;
	
	protected IPersistence<BankBranch, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<BankBranch> getByName(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	public Optional<BankBranch> getByCode(String code) throws ObjectNotFoundException {
		return repository.findByCode(code);
	}

	@Override
	protected Page<BankBranch> performSearch(String keyword, Pageable pageable) {
		return repository.find(keyword, pageable);
	}

	@Override
	protected Optional<BankBranch> fetchBusinessObject(Object key) throws CerberusException {
		return super.getBizObject("findByName", key);
	}
}
