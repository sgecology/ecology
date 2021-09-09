package net.ecology.css.service.config;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import net.ecology.entity.config.Configuration;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.service.IGenericService;

public interface ConfigurationService extends IGenericService<Configuration, Long> {

	/**
	 * Get one Configuration with the provided name.
	 * 
	 * @param code
	 *            The Configuration name
	 * @return The Configuration
	 * @throws ObjectNotFoundException
	 *             If no such Configuration exists.
	 */
	Optional<Configuration> getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Configurations with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Configurations
	 */
	Page<Configuration> getObjects(SearchParameter searchParameter);

	/**
	 * Get list of configurations with the provided group.
	 * 
	 * @param group
	 *            The search group
	 * @return The list of configurations
	 */
	List<Configuration> getByGroup(String group);

	/**
	 * Check if exists of any configurations with the provided group.
	 * 
	 * @param group
	 *            The search group
	 * @return The status of checking by true of exists at least one configuration
	 */
	boolean isExistsByGroup(String group);

	/**
	 * Check if exists of any configurations with the provided name.
	 * 
	 * @param name
	 *            The search name
	 * @return The status of checking by true of exists at least one configuration
	 */
	boolean isExistsByName(String name);
}
