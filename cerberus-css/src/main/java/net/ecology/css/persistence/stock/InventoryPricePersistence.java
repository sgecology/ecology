package net.ecology.css.persistence.stock;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.ecology.entity.stock.InventoryCore;
import net.ecology.entity.stock.InventoryPrice;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface InventoryPricePersistence extends BasePersistence<InventoryPrice, Long> {
	Long countByOwner(InventoryCore inventoryCore);
	List<InventoryPrice> findByOwner(InventoryCore inventoryCore);
}
