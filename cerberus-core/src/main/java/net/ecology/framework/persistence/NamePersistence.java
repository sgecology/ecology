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
public interface NamePersistence<T, PK extends Serializable> extends BasePersistence<T, PK> {
	Optional<T> findByName(String name);
	boolean existsByName(String name);
}
