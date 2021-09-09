package net.ecology.css.service.contact;

import org.springframework.data.domain.Page;

import net.ecology.entity.contact.Contact;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.service.IGenericService;

public interface ContactService extends IGenericService<Contact, Long> {

	/**
	 * Get one Contact with the provided code.
	 * 
	 * @param code
	 *            The Contact code
	 * @return The Contact
	 * @throws ObjectNotFoundException
	 *             If no such Contact exists.
	 */
	Contact getObjectByCode(String code) throws ObjectNotFoundException;

	/**
	 * Get one Contacts with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Contacts
	 */
	Page<Contact> getObjects(SearchParameter searchParameter);
}
