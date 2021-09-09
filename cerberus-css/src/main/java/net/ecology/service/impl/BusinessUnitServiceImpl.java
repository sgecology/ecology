package net.ecology.service.impl;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.ecology.css.persistence.org.BusinessUnitRepository;
import net.ecology.css.service.org.BusinessUnitService;
import net.ecology.css.specification.BusinessUnitSpecification;
import net.ecology.entity.business.BusinessUnit;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.global.GlobeConstants;

@Service
public class BusinessUnitServiceImpl extends GenericService<BusinessUnit, Long> implements BusinessUnitService{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7067548144456556095L;

	@Inject 
	private BusinessUnitRepository repository;
	
	protected IPersistence<BusinessUnit, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public BusinessUnit getObjectByCode(String code) throws ObjectNotFoundException {
		return super.getOptionalObject(repository.findByCode(code));
	}

	@Override
	protected Page<BusinessUnit> performSearch(String keyword, Pageable pageable) {
		Page<BusinessUnit> fetchedResult = repository.find(keyword, pageable);
		//log.info("Fetched results: " + fetchedResult.getSize());
		return fetchedResult;
	}

	/*private BusinessUnit parseEntity(List<String> data){
		//BusinessUnit.buildObject(data);
		return new BusinessUnit();
		return BusinessUnit.getInstance(
				(String)CollectionsUtility.getEntry(data, 0), //Code
				(String)CollectionsUtility.getEntry(data, 2), //First name
				(String)CollectionsUtility.getEntry(data, 1)) //Last name
				.setDateOfBirth(DateTimeUtility.createFreeDate((String)CollectionsUtility.getEntry(data, 4)))
				.setPlaceOfBirth((String)CollectionsUtility.getEntry(data, 5))
				.setNationalId((String)CollectionsUtility.getEntry(data, 6))
				.setNationalIdIssuedDate(DateTimeUtility.createFreeDate((String)CollectionsUtility.getEntry(data, 7)))
				.setNationalIdIssuedPlace((String)CollectionsUtility.getEntry(data, 8))
				.setGender(GenderTypeUtility.getGenderType((String)CollectionsUtility.getEntry(data, 21)))
				.setAddress((String)CollectionsUtility.getEntry(data, 14))
				.setPresentAddress((String)CollectionsUtility.getEntry(data, 14), (String)CollectionsUtility.getEntry(data, 22))
				.setBillingAddress((String)CollectionsUtility.getEntry(data, 15), (String)CollectionsUtility.getEntry(data, 22))
				.setPhones(CommonUtility.safeSubString((String)CollectionsUtility.getEntry(data, 18), 0, 50))
				.setCellPhones(CommonUtility.safeSubString((String)CollectionsUtility.getEntry(data, 19), 0, 50))
				.setOverallExpectation((String)CollectionsUtility.getEntry(data, 28))
				.setOverallExperience((String)CollectionsUtility.getEntry(data, 27))
				.setEmail((String)CollectionsUtility.getEntry(data, 20))
				.setNotes((String)CollectionsUtility.getEntry(data, 29))
			;
	}*/

	@Override
	public Page<BusinessUnit> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(BusinessUnitSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Long count(String countByProperty, Object value) {
		if (GlobeConstants.PROP_CODE.equalsIgnoreCase(countByProperty))
			return this.repository.countByCode(value.toString());

		if (GlobeConstants.PROP_NAME.equalsIgnoreCase(countByProperty))
			return this.repository.countByName((String)value);

		throw new RuntimeException(String.join("Count by property[", countByProperty, "] with value[", (String)value, "] Not implemented yet!"));
	}
}
