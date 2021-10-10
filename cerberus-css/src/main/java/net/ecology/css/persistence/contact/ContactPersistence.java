package net.ecology.css.persistence.contact;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.contact.Contact;
import net.ecology.framework.persistence.CodePersistence;

@Repository
public interface ContactPersistence extends CodePersistence<Contact, Long> {
  @Query(value = "SELECT entity.code FROM #{#entityName} entity ", nativeQuery = true)
  List<String> findCode();

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.firstName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.lastName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.displayName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.title) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.email) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<Contact> search(@Param("keyword") String keyword, Pageable pageable);
}
