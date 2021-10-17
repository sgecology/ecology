/**
 * 
 */
package net.ecology.auth.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.admin.Package;
import net.ecology.entity.admin.PackageAuthority;
import net.ecology.entity.auth.Authority;
import net.ecology.framework.persistence.BasePersistence;

/**
 * @author bqduc
 *
 */
@Repository
public interface PackageAuthorityPersistence extends BasePersistence<PackageAuthority, Long> {
	@Query("SELECT entity FROM #{#entityName} entity WHERE LOWER(entity.authority.name) like LOWER(:keyword) or LOWER(entity.apackage.name) like LOWER(:keyword)")
	List<PackageAuthority> find(@Param("keyword") String keyword);

	List<PackageAuthority> findByApackageAndAuthority(Package apackage, Authority authority);
}
