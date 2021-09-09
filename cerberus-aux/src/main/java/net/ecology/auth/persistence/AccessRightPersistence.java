/**
 * 
 */
package net.ecology.auth.persistence;

import org.springframework.stereotype.Repository;

import net.ecology.entity.admin.AccessRight;
import net.ecology.framework.persistence.BasePersistence;

/**
 * @author bqduc
 *
 */
@Repository
public interface AccessRightPersistence extends BasePersistence<AccessRight, Long> {
	AccessRight findByName(String name);
}
