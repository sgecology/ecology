/**
 * 
 */
package net.ecology.lingual.service;

import net.ecology.entity.i18n.I18nLocale;
import net.ecology.framework.service.IGenericService;

/**
 * @author ducbq
 *
 */
public interface LocaleService extends IGenericService<I18nLocale, Long> {
	I18nLocale getLocale(String language);
}
