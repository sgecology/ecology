package net.ecology.aspect.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.aspect.entity.AspectEntry;
import net.ecology.framework.persistence.IPersistence;

@Repository
public interface AspectEntryPersistence extends IPersistence<AspectEntry, Long>{
  List<AspectEntry> findByObjectId(Long objectId);

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.object) like LOWER(CONCAT('%',:keyword,'%'))"
			+ ")"
	)
	Page<AspectEntry> find(@Param("keyword") String keyword, Pageable pageable);
}
