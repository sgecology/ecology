package net.ecology.css.persistence.stock;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.ecology.entity.stock.InventoryCore;
import net.ecology.entity.stock.InventoryImage;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface InventoryImagePersistence extends BasePersistence<InventoryImage, Long> {
	Long countByOwner(InventoryCore inventoryCore);
	List<InventoryImage> findByOwner(InventoryCore inventoryCore);
}
