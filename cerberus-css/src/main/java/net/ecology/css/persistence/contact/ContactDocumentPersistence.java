package net.ecology.css.persistence.contact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.contact.Contact;
import net.ecology.entity.contact.ContactDocument;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface ContactDocumentPersistence extends BasePersistence<ContactDocument, Long> {
	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.owner.code) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.owner.firstName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.owner.lastName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.owner.displayName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.owner.title) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.owner.email) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<Contact> search(@Param("keyword") String keyword, Pageable pageable);
}
