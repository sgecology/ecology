/**
 * 
 */
package net.nep.facade;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.common.CollectionsUtility;
import net.ecology.domain.entity.Attachment;
import net.ecology.domain.model.dso.DsoRoot;
import net.ecology.entity.stock.InventoryCore;
import net.ecology.entity.stock.InventoryDetail;
import net.ecology.entity.stock.InventoryImage;
import net.ecology.entity.stock.InventoryPrice;

/**
 * @author ducbq
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ProductProfile extends DsoRoot {
	private static final long serialVersionUID = -1131146194417567413L;

	private InventoryCore core;
	private InventoryDetail profile;
	private InventoryPrice profileDetail;

	@Builder.Default
	private List<Attachment> images = CollectionsUtility.newList();

	@Builder.Default
	List<InventoryImage> inventoryImages = CollectionsUtility.newList();

	@Builder.Default
	private Boolean changedImages = Boolean.FALSE;

	private String serial;

	public void addAttachment(Attachment attachment) {
		images.add(attachment);
	}
}
