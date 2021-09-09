package net.ecology.service.general;

import java.util.Optional;

import org.springframework.data.domain.Page;

import net.ecology.entity.general.Catalogue;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.service.IGenericService;

public interface CatalogueService extends IGenericService<Catalogue, Long> {

	/**
	 * Get one Catalogue with the provided name.
	 * 
	 * @param name
	 *            The Catalogue name
	 * @return The Catalogue
	 * @throws ObjectNotFoundException
	 *             If no such Catalogue exists.
	 */
	Optional<Catalogue> getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Catalogue with the provided code.
	 * 
	 * @param code
	 *            The Catalogue code
	 * @return The Catalogue
	 * @throws ObjectNotFoundException
	 *             If no such Catalogue exists.
	 */
	Optional<Catalogue> getByCode(String code) throws ObjectNotFoundException;

	/**
	 * Get one Catalogues with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Catalogues
	 */
	Page<Catalogue> getObjects(SearchParameter searchParameter);

}
