package net.ecology.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.css.persistence.stock.InventoryCorePersistence;
import net.ecology.css.persistence.stock.InventoryDetailPersistence;
import net.ecology.css.persistence.stock.InventoryImagePersistence;
import net.ecology.css.service.stock.InventoryService;
import net.ecology.domain.entity.Attachment;
import net.ecology.entity.stock.InventoryCore;
import net.ecology.entity.stock.InventoryDetail;
import net.ecology.entity.stock.InventoryImage;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.nep.facade.ProductProfile;


@Service
public class InventoryServiceImpl extends GenericService<InventoryCore, Long> implements InventoryService {
	private static final long serialVersionUID = 7785962811434327239L;

	@Inject 
	private InventoryCorePersistence repository;

	@Inject 
	private InventoryImagePersistence inventoryImageRepository;

	@Inject 
	private InventoryDetailPersistence inventoryDetailRepository;

	protected IPersistence<InventoryCore, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public ProductProfile getProfile(Long objectId) {
		InventoryCore productCore = super.getById(objectId);

		List<InventoryImage> inventoryImages = inventoryImageRepository.findByOwner(productCore);
		//Finally, create profile and associate dependencies
		ProductProfile profileFacade = ProductProfile.builder()
				.core(productCore)
				.inventoryImages(inventoryImages)
		.build();

		return profileFacade;
	}

	@Override
	public InventoryImage saveInventoryImage(InventoryImage inventoryImage) {
		return inventoryImageRepository.saveAndFlush(inventoryImage);
	}

	@Override
	public InventoryDetail saveInventoryImage(InventoryDetail inventoryDetail) {
		return this.inventoryDetailRepository.saveAndFlush(inventoryDetail);
	}

	@Override
	public ProductProfile saveProfile(ProductProfile productProfile) {
		this.repository.saveAndFlush(productProfile.getCore());

		//Save images. Delete all currents and 
		//if (Boolean.TRUE.equals(productProfile.getChangedImages())) {
		for (Attachment attachment :productProfile.getImages()) {
			this.inventoryImageRepository.saveAndFlush(
  			InventoryImage.builder()
  			.owner(productProfile.getCore())
  			.contentType(attachment.getMimetype())
  			.imageName(attachment.getName())
  			.imageBuffer(attachment.getData())
  			.build()
			);
		}
		//}
		return productProfile;
	}

	@Override
	public List<InventoryImage> getInventoryImages(Long inventoryId) {
		InventoryCore inventory = this.getById(inventoryId);
		return inventoryImageRepository.findByOwner(inventory);
	}

	@Override
	public InventoryImage getInventoryImage(Long inventoryImageId) {
		return inventoryImageRepository.findById(inventoryImageId).orElse(null);
	}
}
