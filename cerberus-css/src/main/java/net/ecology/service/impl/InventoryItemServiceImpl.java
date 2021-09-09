package net.ecology.service.impl;

import javax.inject.Inject;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.ecology.css.persistence.stock.InventoryItemPersistence;
import net.ecology.css.service.stock.InventoryItemService;
import net.ecology.css.specification.InventoryItemSpecification;
import net.ecology.entity.stock.InventoryItem;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class InventoryItemServiceImpl extends GenericService<InventoryItem, Long> implements InventoryItemService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7761477574156308888L;

	@Inject 
	private InventoryItemPersistence repository;
	
	protected IPersistence<InventoryItem, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public InventoryItem getByBarcode(String barcode) throws ObjectNotFoundException {
		return (InventoryItem)super.getOptionalObject(repository.findByBarcode(barcode));
	}

	@Override
	protected Specification<InventoryItem> getRepoSpecification(SearchParameter searchParameter) {
		return InventoryItemSpecification.buildSpecification(searchParameter);
	}
}
