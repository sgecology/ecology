package net.ecology.auth.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.auth.persistence.DivisionAuthorityPersistence;
import net.ecology.auth.service.DivisionAuthorityService;
import net.ecology.entity.admin.Division;
import net.ecology.entity.admin.DivisionAuthority;
import net.ecology.entity.auth.Authority;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class DivisionAuthorityServiceImpl extends GenericService<DivisionAuthority, Long> implements DivisionAuthorityService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3269698913225344087L;

	@Override
	public List<DivisionAuthority> get(Division module, Authority authority) throws ObjectNotFoundException {
		return this.repository.findByModuleAndAuthority(module, authority);
	}

	@Inject 
	private DivisionAuthorityPersistence repository;
	
	protected IPersistence<DivisionAuthority, Long> getPersistence() {
		return this.repository;
	}
}
