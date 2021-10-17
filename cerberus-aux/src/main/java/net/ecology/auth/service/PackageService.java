package net.ecology.auth.service;

import net.ecology.entity.admin.Package;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface PackageService extends IGenericService<Package, Long> {

	/**
	 * Get one Module with the provided code.
	 * 
	 * @param name
	 *            The Module name
	 * @return The Module
	 * @throws ObjectNotFoundException
	 *             If no such Module exists.
	 */
	Package getByName(String name) throws ObjectNotFoundException;
}
