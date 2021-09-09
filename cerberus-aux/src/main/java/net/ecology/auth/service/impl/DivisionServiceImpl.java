package net.ecology.auth.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.auth.persistence.DivisionPersistence;
import net.ecology.auth.service.DivisionService;
import net.ecology.entity.admin.Division;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;

@Service
public class DivisionServiceImpl extends GenericService<Division, Long> implements DivisionService{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1074949018455408392L;

	@Inject 
	private DivisionPersistence repository;
	
	protected IPersistence<Division, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Division getByName(String name) throws ObjectNotFoundException {
		return (Division)super.getOptionalObject(repository.findByName(name));
	}
}
