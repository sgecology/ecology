package net.ecology.css.persistence.general;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.general.GeneralCatalogue;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface ItemRepository extends BasePersistence<GeneralCatalogue, Long>{
	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<GeneralCatalogue> search(@Param("keyword") String keyword, Pageable pageable);

	GeneralCatalogue findByCode(String code);
	GeneralCatalogue findByName(String name);
}
