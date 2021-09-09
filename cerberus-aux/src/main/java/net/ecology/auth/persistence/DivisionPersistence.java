/**
 * 
 */
package net.ecology.auth.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.admin.Division;
import net.ecology.framework.persistence.NamePersistence;

/**
 * @author bqduc
 *
 */
@Repository
public interface DivisionPersistence extends NamePersistence<Division, Long> {

	@Query("SELECT entity FROM #{#entityName} entity WHERE LOWER(entity.name) like LOWER(:keyword) ")
	List<Division> find(@Param("keyword") String keyword);
}
