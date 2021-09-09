package net.ecology.css.persistence.stock;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.ecology.entity.stock.InventoryCore;
import net.ecology.entity.stock.InventoryDetail;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface InventoryDetailPersistence extends BasePersistence<InventoryDetail, Long> {
	Long countByOwner(InventoryCore inventoryCore);
	List<InventoryDetail> findByOwner(InventoryCore inventoryCore);
}
