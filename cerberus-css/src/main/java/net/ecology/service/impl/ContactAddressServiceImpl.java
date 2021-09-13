package net.ecology.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import net.ecology.css.persistence.contact.ContactAddressPersistence;
import net.ecology.css.service.contact.ContactAddressService;
import net.ecology.css.specification.ContactAddressSpecification;
import net.ecology.entity.contact.Contact;
import net.ecology.entity.contact.ContactAddress;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class ContactAddressServiceImpl extends GenericService<ContactAddress, Long> implements ContactAddressService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3828332078384811862L;
	@Inject 
	private ContactAddressPersistence repository;
	
	protected IPersistence<ContactAddress, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public List<ContactAddress> getByContact(Contact contact) throws ObjectNotFoundException {
		return repository.findByOwner(contact);
	}

	@Override
	public Page<ContactAddress> getObjects(SearchParameter searchParameter) {
		Page<ContactAddress> fetchedObjects = this.repository.findAll(ContactAddressSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
		//Perform additional operations here
		return fetchedObjects;
	}
}
