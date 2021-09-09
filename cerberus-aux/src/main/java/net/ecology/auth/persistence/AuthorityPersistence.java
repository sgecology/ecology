/**
 * 
 */
package net.ecology.auth.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.auth.Authority;
import net.ecology.framework.persistence.NamePersistence;

/**
 * @author ducbui
 *
 */
@Repository
public interface AuthorityPersistence extends NamePersistence <Authority, Long>{
	
	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ "			LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%'))"
			+ " or	LOWER(entity.info) like LOWER(CONCAT('%',:keyword,'%'))"
			+ ")"
	)
	Page<Authority> search(@Param("keyword") String keyword, Pageable pageable);
}
