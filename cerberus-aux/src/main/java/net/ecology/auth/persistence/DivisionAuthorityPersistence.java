/**
 * 
 */
package net.ecology.auth.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.admin.Division;
import net.ecology.entity.admin.DivisionAuthority;
import net.ecology.entity.auth.Authority;
import net.ecology.framework.persistence.BasePersistence;

/**
 * @author bqduc
 *
 */
@Repository
public interface DivisionAuthorityPersistence extends BasePersistence<DivisionAuthority, Long> {
	@Query("SELECT entity FROM #{#entityName} entity WHERE LOWER(entity.authority.name) like LOWER(:keyword) or LOWER(entity.module.name) like LOWER(:keyword)")
	List<DivisionAuthority> find(@Param("keyword") String keyword);

	List<DivisionAuthority> findByModuleAndAuthority(Division module, Authority authority);
}
