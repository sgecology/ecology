/**
 * 
 */
package net.ecology.framework.persistence;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author bqduc
 *
 */
@NoRepositoryBean
public interface IPersistence<T, PK extends Serializable> extends JpaRepository<T, PK>, JpaSpecificationExecutor<T> {
	Optional<T> findById(PK id);

	Page<T> findAll(Pageable pageable);
	Page<T> findAllByOrderByIdAsc(Pageable pageable);
}
