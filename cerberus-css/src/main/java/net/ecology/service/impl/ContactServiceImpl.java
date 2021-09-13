package net.ecology.service.impl;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.css.persistence.contact.ContactPersistence;
import net.ecology.css.service.contact.ContactService;
import net.ecology.css.specification.ContactSpecification;
import net.ecology.entity.contact.Contact;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class ContactServiceImpl extends GenericService<Contact, Long> implements ContactService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5098808323294021160L;
	@Inject 
	private ContactPersistence repository;

	//@Inject 
	//private BusinessAccountRepository businessAccountRepository;

	protected IPersistence<Contact, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Contact getObjectByCode(String code) throws ObjectNotFoundException {
		return (Contact)super.getOptionalObject(repository.findByCode(code));
	}

	@Override
	protected Page<Contact> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Contact> getObjects(SearchParameter searchParameter) {
		Page<Contact> pagedProducts = this.repository.findAll(ContactSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
		//Perform additional operations here
		return pagedProducts;
	}
}
