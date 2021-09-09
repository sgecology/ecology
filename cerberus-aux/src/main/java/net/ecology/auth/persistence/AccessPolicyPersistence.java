/**
 * 
 */
package net.ecology.auth.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.ecology.entity.auth.AccessPolicy;
import net.ecology.framework.persistence.BasePersistence;

/**
 * @author bqduc
 *
 */
@Repository
public interface AccessPolicyPersistence extends BasePersistence<AccessPolicy, Long> {
	List<AccessPolicy> findByAccessPattern(String accessPattern);
	
	boolean existsByAccessPattern(String accessPattern);
}
