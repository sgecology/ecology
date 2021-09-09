/**
 * 
 */
package net.ecology.framework.persistence;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author bqduc
 *
 */
@NoRepositoryBean
public interface CodePersistence<T, PK extends Serializable> extends BasePersistence<T, PK> {
	Optional<T> findByCode(String code);
}
