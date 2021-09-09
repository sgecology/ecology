package net.ecology.css.service.stock;

import java.util.Optional;

import org.springframework.data.domain.Page;

import net.ecology.entity.stock.InventoryItem;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.service.IGenericService;

public interface InventoryItemService extends IGenericService<InventoryItem, Long> {

	/**
	 * Get one InventoryItem with the provided code.
	 * 
	 * @param code
	 *            The InventoryItem code
	 * @return The InventoryItem
	 * @throws ObjectNotFoundException
	 *             If no such InventoryItem exists.
	 */
	Optional<InventoryItem> getByCode(String code) throws ObjectNotFoundException;

	/**
	 * Get one InventoryItem with the provided barcode.
	 * 
	 * @param barcode
	 *            The InventoryItem barcode
	 * @return The InventoryItem
	 * @throws ObjectNotFoundException
	 *             If no such InventoryItem exists.
	 */
	InventoryItem getByBarcode(String barcode) throws ObjectNotFoundException;

	/**
	 * Get one Enterprises with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Enterprises
	 */
	Page<InventoryItem> getObjects(SearchParameter searchParameter);
}
