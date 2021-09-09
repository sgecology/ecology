package net.ecology.auth.service;

import java.util.List;

import net.ecology.entity.admin.Division;
import net.ecology.entity.admin.DivisionAuthority;
import net.ecology.entity.auth.Authority;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface DivisionAuthorityService extends IGenericService<DivisionAuthority, Long> {

	/**
	 * Get one Module with the provided code.
	 * 
	 * @param module
	 *            The Module
	 * @param authority
	 *            The Authority
	 * @return The list
	 * @throws ObjectNotFoundException
	 *             If no such Module exists.
	 */
	List<DivisionAuthority> get(Division module, Authority authority) throws ObjectNotFoundException;
}
