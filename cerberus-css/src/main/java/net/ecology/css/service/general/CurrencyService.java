package net.ecology.css.service.general;

import java.util.Optional;

import org.springframework.data.domain.Page;

import net.ecology.entity.general.Currency;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.service.IGenericService;

public interface CurrencyService extends IGenericService<Currency, Long> {

	/**
	 * Get one Currency with the provided alphabetic code.
	 * 
	 * @param alphabeticCode
	 *            The Currency alphabetic code
	 * @return The Currency
	 * @throws ObjectNotFoundException
	 *             If no such Currency exists.
	 */
	Optional<Currency> getByCode(String alphabeticCode) throws ObjectNotFoundException;

	/**
	 * Get one Measure units with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Measure units
	 */
	Page<Currency> getObjects(SearchParameter searchParameter);

}
