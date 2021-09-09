/**
 * 
 */
package net.ecology.dmx.helper;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.css.service.config.IGeneralItemService;

/**
 * @author ducbq
 *
 */
@Component
public class MarshallingDataHelper {
	@Inject 
	private IGeneralItemService itemService;

	/*public GeneralCatalogue searchLocalizedItem(String localizedItem, String languageCode) {
		PageRequest pageRequest = PageRequest.of(GlobeConstants.DEFAULT_PAGE_BEGIN, GlobeConstants.DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "id");
		Page<LocalizedItem> pagedItems = itemService.searchLocalizedItems(localizedItem, languageCode, pageRequest);
		return pagedItems.getContent().get(0).getItem();
	}*/
}
