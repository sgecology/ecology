package net.ecology.service.impl;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.ecology.css.persistence.general.MeasureUnitPersistence;
import net.ecology.css.service.general.MeasureUnitService;
import net.ecology.css.service.system.SequenceManager;
import net.ecology.css.specification.MeasureUnitSpecification;
import net.ecology.entity.general.MeasureUnit;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.global.GlobalConstants;
import net.ecology.global.GlobeConstants;

@Service
public class MeasureUnitServiceImpl extends GenericService<MeasureUnit, Long> implements MeasureUnitService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4804189794303411453L;
	@Inject 
	private MeasureUnitPersistence repository;

	@Inject
	private SequenceManager sequenceManager;

	/*@Inject 
	private SequenceService systemSequenceService;*/

	protected IPersistence<MeasureUnit, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<MeasureUnit> getByName(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	protected Page<MeasureUnit> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<MeasureUnit> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(MeasureUnitSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}

	@Override
	protected Optional<MeasureUnit> fetchBusinessObject(Object key) throws ObjectNotFoundException {
		return super.getBizObject("findByName", key);
	}

	@Override
	public Optional<MeasureUnit> getByNameLocale(String nameLocal) throws ObjectNotFoundException {
		return super.getBizObject("findByNameLocal", nameLocal);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Long count(String countByProperty, Object value) {
		if (GlobeConstants.PROP_CODE.equalsIgnoreCase(countByProperty))
			return this.repository.countByCode((String)value);

		if (GlobeConstants.PROP_NAME.equalsIgnoreCase(countByProperty))
			return this.repository.countByName((String)value);

		throw new RuntimeException(String.join("Count by property[", countByProperty, "] with value[", (String)value, "] Not implemented yet!"));
	}

	@Override
	public String nextSerial(String prefix) throws CerberusException {
		String newSerialNo = this.sequenceManager.getNewNumber(prefix, Integer.valueOf(GlobalConstants.SIZE_CODE));
		newSerialNo = prefix + newSerialNo.substring(prefix.length());
		return newSerialNo;
	}
}
