package net.ecology.service.trade;

import java.util.Optional;

import net.ecology.entity.trade.Tax;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface TaxService extends IGenericService<Tax, Long> {

	/**
	 * Get one Tax with the provided name.
	 * 
	 * @param name
	 *            The Tax name
	 * @return The Tax
	 * @throws ObjectNotFoundException
	 *             If no such Tax exists.
	 */
	Optional<Tax> getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Tax with the provided code.
	 * 
	 * @param code
	 *            The Tax code
	 * @return The Tax
	 * @throws ObjectNotFoundException
	 *             If no such Tax exists.
	 */
	Optional<Tax> getByCode(String code) throws ObjectNotFoundException;
}
