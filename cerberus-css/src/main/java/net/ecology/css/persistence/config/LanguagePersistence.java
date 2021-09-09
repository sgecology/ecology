package net.ecology.css.persistence.config;

import org.springframework.stereotype.Repository;

import net.ecology.entity.general.Language;
import net.ecology.framework.persistence.CodeNamePersistence;

@Repository("languageRepository")
public interface LanguagePersistence extends CodeNamePersistence<Language, Long>{
}
