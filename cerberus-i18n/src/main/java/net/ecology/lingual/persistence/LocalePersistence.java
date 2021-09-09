/**
 * 
 */
package net.ecology.lingual.persistence;

import org.springframework.stereotype.Repository;

import net.ecology.entity.i18n.I18nLocale;
import net.ecology.framework.persistence.BasePersistence;

/**
 * @author ducbq
 *
 */
@Repository
public interface LocalePersistence extends BasePersistence<I18nLocale, Long> {
	I18nLocale findByLanguage(String language);
	boolean existsByLanguage(String language);
}
