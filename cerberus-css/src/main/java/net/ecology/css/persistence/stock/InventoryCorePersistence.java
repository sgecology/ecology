package net.ecology.css.persistence.stock;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.stock.InventoryCore;
import net.ecology.entity.stock.InventoryDetail;
import net.ecology.framework.persistence.CodeNamePersistence;

@Repository
public interface InventoryCorePersistence extends CodeNamePersistence<InventoryCore, Long> {
	Long countByBarcode(String barcode);
	Optional<InventoryDetail> findByBarcode(String barcode);

	@Query(value = "SELECT entity.code FROM #{#entityName} entity ", nativeQuery = true)
  List<String> findCode();

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%'))"
			+ " or LOWER(entity.barcode) like LOWER(CONCAT('%',:keyword,'%'))"
			+ " or LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%'))"
			+ " or LOWER(entity.labelName) like LOWER(CONCAT('%',:keyword,'%'))"
			+ " or LOWER(entity.translatedName) like LOWER(CONCAT('%',:keyword,'%'))"
			+ ")"
	)
	Page<InventoryCore> find(@Param("keyword") String keyword, Pageable pageable);
}
