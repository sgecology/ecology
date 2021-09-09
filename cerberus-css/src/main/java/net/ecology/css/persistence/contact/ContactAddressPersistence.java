package net.ecology.css.persistence.contact;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.ecology.entity.contact.Contact;
import net.ecology.entity.contact.ContactAddress;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface ContactAddressPersistence extends BasePersistence<ContactAddress, Long> {
	List<ContactAddress> findByOwner(Contact contact);
}
