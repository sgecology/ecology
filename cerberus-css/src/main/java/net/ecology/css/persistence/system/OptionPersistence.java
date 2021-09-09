package net.ecology.css.persistence.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.system.Option;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface OptionPersistence extends BasePersistence<Option, Long>{
	Option findByUserAndKey(String user, String key);
	List<Option> findByKey(String key);

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.user) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.key) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<Option> search(@Param("keyword") String keyword, Pageable pageable);
}
