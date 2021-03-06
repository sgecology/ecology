package net.ecology.auth.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.auth.constants.GlobeAuxConstants;
import net.ecology.auth.persistence.AuthorityPersistence;
import net.ecology.auth.service.AuthorityService;
import net.ecology.entity.auth.Authority;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class AuthorityServiceImpl extends GenericService<Authority, Long> implements AuthorityService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 154324205536713910L;
	@Inject 
	private AuthorityPersistence repository;
	
	protected IPersistence<Authority, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Authority getByName(String name) throws ObjectNotFoundException {
		Optional<Authority> opt = repository.findByName(name);
		return opt.orElse(null);
	}

	@Override
	public Authority getMinimumUserAuthority() throws ObjectNotFoundException {
		Authority fetchedResult = null;
		Optional<Authority> optAuthority = this.repository.findByName(GlobeAuxConstants.MINIMUM_USER_AUTHORITY);
		if (!optAuthority.isPresent()) {
			fetchedResult = Authority.builder().name(GlobeAuxConstants.MINIMUM_USER_AUTHORITY).displayName("Default minimum authority").build();
			this.repository.save(fetchedResult);
		} else {
			fetchedResult = optAuthority.get();
		}
		return fetchedResult;
	}
}
