package net.ecology.css.persistence.config;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.ecology.entity.general.GeneralCatalogue;
import net.ecology.entity.general.Language;
import net.ecology.entity.general.LocalizedItem;
import net.ecology.framework.persistence.BasePersistence;

@Repository
public interface LocalizedItemPersistence extends BasePersistence<LocalizedItem, Long>{

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " entity.item = :item and entity.language = :language"
			+ ")"
	)
	public LocalizedItem findByLocalizedItem(Language language, GeneralCatalogue item);
}
