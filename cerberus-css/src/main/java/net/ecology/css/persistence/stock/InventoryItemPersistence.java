package net.ecology.css.persistence.stock;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.stock.InventoryItem;
import net.ecology.framework.persistence.CodeNamePersistence;

@Repository
public interface InventoryItemPersistence extends CodeNamePersistence<InventoryItem, Long> {
	Optional<InventoryItem> findByBarcode(String barcode);

	@Query(value = "SELECT entity.code FROM #{#entityName} entity ", nativeQuery = true)
  List<String> findCode();

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.barcode) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.translatedName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.composition) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<InventoryItem> search(@Param("keyword") String keyword, Pageable pageable);
}
