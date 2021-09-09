package net.ecology.css.service.config;

import net.ecology.entity.general.GeneralCatalogue;
import net.ecology.entity.general.Language;
import net.ecology.entity.general.LocalizedItem;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface IGeneralItemService extends IGenericService<GeneralCatalogue, Long>{

  /**
   * Get one item with the provided code.
   * 
   * @param code The item code
   * @return The item
   * @throws ObjectNotFoundException If no such account exists.
   */
	GeneralCatalogue getObjectByCode(String code) throws ObjectNotFoundException;

  /**
   * Get one item with the provided code.
   * 
   * @param name The item name
   * @return The item
   * @throws ObjectNotFoundException If no such account exists.
   */
	GeneralCatalogue getByName(String name) throws ObjectNotFoundException;

	//Page<LocalizedItem> searchLocalizedItems(String keyword, String languageCode, Pageable pageable);

	LocalizedItem getLocalizedItem(GeneralCatalogue item, Language language);

	LocalizedItem saveLocalizedItem(LocalizedItem localizedItem);
	
	//List<LocalizedItem> getLocalizedItems(String subtype, Language language);
}
