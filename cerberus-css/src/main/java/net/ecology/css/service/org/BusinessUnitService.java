package net.ecology.css.service.org;

import org.springframework.data.domain.Page;

import net.ecology.entity.business.BusinessUnit;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.service.IGenericService;

public interface BusinessUnitService extends IGenericService<BusinessUnit, Long>{

  /**
   * Get one business unit with the provided code.
   * 
   * @param code The business unit code
   * @return The business unit
   * @throws ObjectNotFoundException If no such business unit exists.
   */
	BusinessUnit getObjectByCode(String code) throws ObjectNotFoundException;

  /**
   * Get one business units with the provided search parameters.
   * 
   * @param searchParameter The search parameter
   * @return The pageable business units
   */
	Page<BusinessUnit> getObjects(SearchParameter searchParameter);
}
