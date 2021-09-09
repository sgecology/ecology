package net.ecology.aspect.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.aspect.entity.AspectEntry;
import net.ecology.aspect.repository.AspectEntryPersistence;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class AspectEntryService extends GenericService<AspectEntry, Long> implements IAspectEntryService {
	private static final long serialVersionUID = 5204079684400280912L;

	@Inject 
	private AspectEntryPersistence repository;

	protected IPersistence<AspectEntry, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public List<AspectEntry> getAspectObjects(Long objectId, String object) {
		// TODO Auto-generated method stub
		return null;
	}
}
