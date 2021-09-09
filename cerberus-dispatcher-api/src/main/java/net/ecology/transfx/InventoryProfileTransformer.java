/**
 * 
 */
package net.ecology.transfx;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.css.service.stock.InventoryService;
import net.ecology.entity.stock.InventoryCore;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.entity.RepoObject;
import net.nep.facade.ProductProfile;
import net.peaga.domain.base.Repository;
import net.peaga.domain.stock.InventoryItemProxy;

/**
 * @author ducbq
 *
 */
@Component
public class InventoryProfileTransformer implements DataTransformer {
	@Inject
	private InventoryService businessService;

	public ProductProfile marshall(InventoryItemProxy inventoryItemProxy) {
		InventoryCore inventoryCore = this.businessService.getObject(inventoryItemProxy.getId());
		if (null==inventoryCore) {
			inventoryCore = InventoryCore.builder().build();
		}
		return null;
	}

	public InventoryItemProxy unmarshall(ProductProfile productProfile) {
		InventoryItemProxy inventoryItemProxy = InventoryItemProxy.builder()
				.code(productProfile.getCore().getCode())
				.barcode(productProfile.getCore().getBarcode())
				.name(productProfile.getCore().getName())
				.labelName(productProfile.getCore().getLabelName())
				.translatedName(productProfile.getCore().getTranslatedName())
				.build();

		if (null != productProfile.getInventoryImages() && productProfile.getInventoryImages().size() > 0) {
			inventoryItemProxy.setImageData(productProfile.getInventoryImages().get(0).getImageBuffer());
		}

		return inventoryItemProxy;
	}

	@Override
	public RepoObject transformToBusiness(final Repository proxyObject, RepoObject targetBusinessObject) throws CerberusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Repository transformToProxy(final RepoObject businessObject, Repository targetProxyObject) throws CerberusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepoObject transformToBusiness(Repository proxyObject, RepoObject targetBusinessObject, String[] excludedAttributes) throws CerberusException {
		// TODO Auto-generated method stub
		return null;
	}
}
