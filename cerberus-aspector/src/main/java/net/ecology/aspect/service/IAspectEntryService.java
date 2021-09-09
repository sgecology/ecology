package net.ecology.aspect.service;

import java.util.List;

import net.ecology.aspect.entity.AspectEntry;
import net.ecology.framework.service.IGenericService;

public interface IAspectEntryService extends IGenericService<AspectEntry, Long> {

	/**
	 * Get the list of aspect objects.
	 * 
	 * @param object
	 *            The AspectEntry's object name
	 * @param objectId
	 *            The AspectEntry's object id
	 * @return The list of AspectEntry objects
	 */
	List<AspectEntry> getAspectObjects(Long objectId, String object);
}
