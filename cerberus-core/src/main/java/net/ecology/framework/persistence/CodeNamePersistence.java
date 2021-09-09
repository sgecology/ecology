/**
 * 
 */
package net.ecology.framework.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

/**
 * @author bqduc
 *
 */
@NoRepositoryBean
public interface CodeNamePersistence<T, PK extends Serializable> extends BasePersistence<T, PK> {
	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<T> find(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT entity FROM #{#entityName} entity WHERE LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) or LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%')) ")
	List<T> find(@Param("keyword") String keyword);
	
	Optional<T> findByCode(String code);
	Optional<T> findByName(String name);

	Long countByCode(String code);
	Long countByName(String name);

	/**
	 * Check if a user with the code exists in the system
	 * 
	 * @param code
	 *            the entity's code
	 * @return a true if exists
	 */
	boolean existsByCode(String code);

	/**
	 * Check if a user with the name exists in the system
	 * 
	 * @param name
	 *            the entity's name
	 * @return a true if exists
	 */
	boolean existsByName(String name);
}
