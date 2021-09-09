/**
 * 
 */
package net.ecology.auth.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.auth.GrantedAccessPolicy;
import net.ecology.framework.persistence.BasePersistence;

/**
 * @author bqduc
 *
 */
@Repository
public interface GrantedAccessPolicyPersistence extends BasePersistence<GrantedAccessPolicy, Long> {
	List<GrantedAccessPolicy> findByAuthority(Authority authority);

	boolean existsByAuthority(Authority authority);

	boolean existsByAccessPolicyAndAuthority(AccessPolicy accessPolicy, Authority authority);
}