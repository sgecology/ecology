package net.ecology.auth.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.auth.persistence.PackagePersistence;
import net.ecology.auth.service.PackageService;
import net.ecology.entity.admin.Package;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;

@Service
public class PackageServiceImpl extends GenericService<Package, Long> implements PackageService{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1074949018455408392L;

	@Inject 
	private PackagePersistence repository;
	
	protected IPersistence<Package, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Package getByName(String name) throws ObjectNotFoundException {
		return (Package)super.getOptionalObject(repository.findByName(name));
	}
}
