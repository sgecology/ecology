package net.ecology.lingual.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.entity.i18n.I18nLocale;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.lingual.persistence.LocalePersistence;

@Service
public class LocaleServiceImpl extends GenericService<I18nLocale, Long> implements LocaleService {
	private static final long serialVersionUID = -1184446978057141786L;

	@Inject 
	private LocalePersistence persistence;
	
	protected IPersistence<I18nLocale, Long> getPersistence() {
		return this.persistence;
	}

	@Override
	public I18nLocale getLocale(String language) {
		return persistence.findByLanguage(language);
	}
}
