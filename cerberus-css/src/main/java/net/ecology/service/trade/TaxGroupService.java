package net.ecology.service.trade;

import java.util.Optional;

import net.ecology.entity.general.TaxGroup;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface TaxGroupService extends IGenericService<TaxGroup, Long> {

	/**
	 * Get one TaxGroup with the provided name.
	 * 
	 * @param name
	 *            The TaxGroup name
	 * @return The TaxGroup
	 * @throws ObjectNotFoundException
	 *             If no such TaxGroup exists.
	 */
	Optional<TaxGroup> getByName(String name) throws ObjectNotFoundException;
}
