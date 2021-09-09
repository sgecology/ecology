package net.ecology.auth.service;

import net.ecology.entity.admin.Division;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface DivisionService extends IGenericService<Division, Long> {

	/**
	 * Get one Module with the provided code.
	 * 
	 * @param name
	 *            The Module name
	 * @return The Module
	 * @throws ObjectNotFoundException
	 *             If no such Module exists.
	 */
	Division getByName(String name) throws ObjectNotFoundException;
}
