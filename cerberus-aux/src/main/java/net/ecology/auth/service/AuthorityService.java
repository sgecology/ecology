package net.ecology.auth.service;

import org.springframework.data.domain.Page;

import net.ecology.entity.auth.Authority;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.service.IGenericService;

public interface AuthorityService extends IGenericService<Authority, Long> {

	/**
	 * Get one Authority with the provided code.
	 * 
	 * @param name
	 *            The Authority name
	 * @return The Authority
	 * @throws ObjectNotFoundException
	 *             If no such Authority exists.
	 */
	Authority getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Authoritys with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Authorities
	 */
	Page<Authority> getObjects(SearchParameter searchParameter);

	
	/**
	 * Get one Authority with the provided code.
	 * 
	 * @param name
	 *            The Authority name
	 * @return The Authority
	 * @throws ObjectNotFoundException
	 *             If no such Authority exists.
	 */
	Authority getMinimumUserAuthority() throws ObjectNotFoundException;
}
