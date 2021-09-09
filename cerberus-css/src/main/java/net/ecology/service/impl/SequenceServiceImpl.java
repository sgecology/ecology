package net.ecology.service.impl;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.css.persistence.system.SystemSequencePersistence;
import net.ecology.css.service.system.SequenceService;
import net.ecology.css.specification.SequenceSpecification;
import net.ecology.entity.system.Sequence;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;

@Service
public class SequenceServiceImpl extends GenericService<Sequence, Long> implements SequenceService{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5209863588217204283L;

	@Inject 
	private SystemSequencePersistence repository;
	
	protected IPersistence<Sequence, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Sequence getObjectBySerial(String serial) throws ObjectNotFoundException {
		return super.getOptionalObject(repository.findBySerial(serial));
	}

	@Override
	protected Page<Sequence> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Sequence> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(SequenceSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}
}
