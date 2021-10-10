package net.ecology.css.service.contact;

import java.util.List;

import net.ecology.entity.contact.Contact;
import net.ecology.entity.contact.ContactAddress;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface ContactAddressService extends IGenericService<ContactAddress, Long> {

	/**
	 * Get one contact-address with the provided contact.
	 * 
	 * @param code
	 *            The Contact entity
	 * @return The list of contact-addresses
	 * @throws ObjectNotFoundException
	 *             If no such contact-address exists.
	 */
	List<ContactAddress> getByContact(Contact contact) throws ObjectNotFoundException;
}
