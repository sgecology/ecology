package net.ecology.auth.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.auth.constants.AuxConstants;
import net.ecology.auth.persistence.AuthorityPersistence;
import net.ecology.auth.service.AuthorityService;
import net.ecology.entity.auth.Authority;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class AuthorityServiceImpl extends GenericService<Authority, Long> implements AuthorityService{
	private static final long serialVersionUID = 7761477574156308888L;

	@Inject 
	private AuthorityPersistence repository;
	
	protected IPersistence<Authority, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Authority getByName(String name) throws ObjectNotFoundException {
		Optional<Authority> opt = repository.findByName(name);
		return (Authority)super.getOptionalObject(repository.findByName(name));
	}

	@Override
	public Authority getMinimumUserAuthority() throws ObjectNotFoundException {
		Authority fetchedResult = null;
		Optional<Authority> optAuthority = this.repository.findByName(AuxConstants.MINIMUM_USER_AUTHORITY);
		if (!optAuthority.isPresent()) {
			fetchedResult = Authority.builder().name(AuxConstants.MINIMUM_USER_AUTHORITY).displayName("Default minimum authority").build();
			this.repository.save(fetchedResult);
		} else {
			fetchedResult = optAuthority.get();
		}
		return fetchedResult;
	}
}
