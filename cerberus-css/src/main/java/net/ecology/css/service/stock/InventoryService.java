package net.ecology.css.service.stock;

import java.util.List;

import net.ecology.entity.stock.InventoryCore;
import net.ecology.entity.stock.InventoryDetail;
import net.ecology.entity.stock.InventoryImage;
import net.ecology.framework.service.IGenericService;
import net.nep.facade.ProductProfile;

public interface InventoryService extends IGenericService<InventoryCore, Long> {
	ProductProfile getProfile(Long objectId);

	InventoryImage saveInventoryImage(InventoryImage inventoryImage);
	List<InventoryImage> getInventoryImages(Long inventoryId);

	InventoryImage getInventoryImage(Long inventoryImageId);
	
	InventoryDetail saveInventoryImage(InventoryDetail inventoryDetail);

	ProductProfile saveProfile(ProductProfile productProfile);
}
