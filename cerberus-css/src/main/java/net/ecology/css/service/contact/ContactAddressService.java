package net.ecology.css.service.contact;

import java.util.List;

import org.springframework.data.domain.Page;

import net.ecology.entity.contact.Contact;
import net.ecology.entity.contact.ContactAddress;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.service.IGenericService;

public interface ContactAddressService extends IGenericService<ContactAddress, Long> {

	/**
	 * Get one Enterprise with the provided code.
	 * 
	 * @param code
	 *            The Enterprise code
	 * @return The Enterprise
	 * @throws ObjectNotFoundException
	 *             If no such Enterprise exists.
	 */
	List<ContactAddress> getByContact(Contact contact) throws ObjectNotFoundException;

	/**
	 * Get one Enterprises with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Enterprises
	 */
	Page<ContactAddress> getObjects(SearchParameter searchParameter);
}
