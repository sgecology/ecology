package net.ecology.auth.service;

import java.util.List;

import net.ecology.entity.admin.Package;
import net.ecology.entity.admin.PackageAuthority;
import net.ecology.entity.auth.Authority;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface PackageAuthorityService extends IGenericService<PackageAuthority, Long> {

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
	List<PackageAuthority> get(Package module, Authority authority) throws ObjectNotFoundException;
}
