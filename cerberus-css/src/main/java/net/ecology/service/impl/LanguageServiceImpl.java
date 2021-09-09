package net.ecology.service.impl;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.css.persistence.config.LanguagePersistence;
import net.ecology.css.service.config.LanguageService;
import net.ecology.entity.general.Language;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;

@Service
public class LanguageServiceImpl extends GenericService<Language, Long> implements LanguageService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7579757413128840936L;

	@Inject
	private LanguagePersistence repository;

	@Override
	protected IPersistence<Language, Long> getPersistence() {
		return this.repository;
	}

	@Override
	protected Page<Language> performSearch(String keyword, Pageable pageable) {
		return this.repository.find(keyword, pageable);
	}

	@Override
	public Language getByName(String name) {
		return super.getOptional(this.repository.findByName(name));
	}
}
