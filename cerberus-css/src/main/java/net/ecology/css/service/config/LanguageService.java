package net.ecology.css.service.config;

import java.util.Optional;

import net.ecology.entity.general.Language;
import net.ecology.framework.service.IGenericService;

public interface LanguageService extends IGenericService<Language, Long>{
	Optional<Language> getByCode(String code);
	Language getByName(String name);
}
