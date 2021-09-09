package net.ecology.service.impl;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.css.persistence.config.LocalizedItemPersistence;
import net.ecology.css.persistence.general.ItemRepository;
import net.ecology.css.service.config.IGeneralItemService;
import net.ecology.entity.general.GeneralCatalogue;
import net.ecology.entity.general.Language;
import net.ecology.entity.general.LocalizedItem;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;

@Service
public class GeneralItemService extends GenericService<GeneralCatalogue, Long> implements IGeneralItemService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8419327405445078475L;

	@Inject
	private ItemRepository repository;

	@Inject
	private LocalizedItemPersistence localizedRepository;

	@Override
	protected IPersistence<GeneralCatalogue, Long> getPersistence() {
		return this.repository;
	}

	@Override
	protected Page<GeneralCatalogue> performSearch(String keyword, Pageable pageable) {
		return this.repository.search(keyword, pageable);
	}

	@Override
	public GeneralCatalogue getObjectByCode(String code) throws ObjectNotFoundException {
		return this.repository.findByCode(code);
	}

	@Override
	public GeneralCatalogue getByName(String name) throws ObjectNotFoundException {
		return this.repository.findByName(name);
	}

	@Override
	public LocalizedItem getLocalizedItem(GeneralCatalogue item, Language language) {
		return this.localizedRepository.findByLocalizedItem(language, item);
	}

	@Override
	public LocalizedItem saveLocalizedItem(LocalizedItem localizedItem) {
		return this.localizedRepository.saveAndFlush(localizedItem);
	}

	/*@Override
	public List<LocalizedItem> getLocalizedItems(String subtype, Language language) {
		EntityManager em = this.getEntityManager();
		return em.createQuery("select li from LocalizedItem li where li.item.subtype = :itemSubtype and li.language = :language")
		.setParameter("itemSubtype", subtype)
		.setParameter("language", language)
		.getResultList();
	}

	@Override
	public Page<LocalizedItem> searchLocalizedItems(String keyword, String languageCode, Pageable pageable) {
		StringBuilder jql = new StringBuilder("select localizedItem from LocalizedItem localizedItem where localizedItem.value = :keyword");
		if (CommonUtility.isNotEmpty(languageCode)) {
			jql.append(" and localizedItem.language.code = :languageCode");
		}

		Query query = this.getEntityManager()
  		.createQuery(jql.toString())
  		.setParameter("keyword", keyword);

		if (CommonUtility.isNotEmpty(languageCode)) {
			query.setParameter("languageCode", languageCode);
		}
		List<LocalizedItem> foundItems = query.getResultList();
		return new PageImpl<>(foundItems, pageable, foundItems.size());
		//SearchParameter searchParameter = SearchParameter.builder()
		//		.keyword(keyword)
		//		.build();
		//
		//return this.localizedRepository.findAll(LocalizedItemSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}
	*/
}
