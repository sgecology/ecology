package net.ecology.css.persistence.stock;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.ecology.entity.stock.InventoryItemProfile;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface InventoryItemProfilePersistence extends BasePersistence<InventoryItemProfile, Long> {
	List<InventoryItemProfile> findByInventoryCode(String inventoryItemCode);
	Long countByInventoryCode(String inventoryItemCode);

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.inventoryCode) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.author) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.notes) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<InventoryItemProfile> search(@Param("keyword") String keyword, Pageable pageable);
}
