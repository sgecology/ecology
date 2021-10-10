package net.ecology.css.service.contact;

import java.util.List;

import net.ecology.entity.contact.Contact;
import net.ecology.entity.contact.ContactDocument;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface ContactDocumentService extends IGenericService<ContactDocument, Long> {

	/**
	 * Get one ContactDocument with the provided contact.
	 * 
	 * @param code
	 *            The Contact contact
	 * @return The list of contact-documents
	 * @throws ObjectNotFoundException
	 *             If no such contact-document exists.
	 */
	List<ContactDocument> getByContact(Contact contact) throws ObjectNotFoundException;
}
