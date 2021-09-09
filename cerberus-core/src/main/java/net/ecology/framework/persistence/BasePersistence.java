/**
 * 
 */
package net.ecology.framework.persistence;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author bqduc
 *
 */
@NoRepositoryBean
public interface BasePersistence<T, PK extends Serializable> extends IPersistence<T, PK> {
	@Query("SELECT entity FROM #{#entityName} entity WHERE entity.visible = true")
	List<T> findVisible();
}
