package net.ecology.auth.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.auth.persistence.PackageAuthorityPersistence;
import net.ecology.auth.service.PackageAuthorityService;
import net.ecology.entity.admin.Package;
import net.ecology.entity.admin.PackageAuthority;
import net.ecology.entity.auth.Authority;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class PackageAuthorityServiceImpl extends GenericService<PackageAuthority, Long> implements PackageAuthorityService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3269698913225344087L;

	@Override
	public List<PackageAuthority> get(Package module, Authority authority) throws ObjectNotFoundException {
		return this.repository.findByApackageAndAuthority(module, authority);
	}

	@Inject 
	private PackageAuthorityPersistence repository;
	
	protected IPersistence<PackageAuthority, Long> getPersistence() {
		return this.repository;
	}
}
