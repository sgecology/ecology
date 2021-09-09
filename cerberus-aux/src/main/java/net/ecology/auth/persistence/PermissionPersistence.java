/**
 * 
 */
package net.ecology.auth.persistence;

import org.springframework.stereotype.Repository;

import net.ecology.entity.admin.Permission;
import net.ecology.framework.persistence.BasePersistence;

/**
 * @author bqduc
 *
 */
@Repository
public interface PermissionPersistence extends BasePersistence<Permission, Long> {
}
