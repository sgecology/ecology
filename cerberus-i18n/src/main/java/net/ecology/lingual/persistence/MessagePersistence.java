/**
 * 
 */
package net.ecology.lingual.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.ecology.entity.i18n.I18nLocale;
import net.ecology.entity.i18n.Message;
import net.ecology.framework.persistence.BasePersistence;

/**
 * @author ducbq
 *
 */
@Repository
public interface MessagePersistence extends BasePersistence<Message, Long> {
	List<Message> findByLocale(I18nLocale locale);

	Message findByKeyAndLocale(String key, I18nLocale locale);

	boolean existsByKey(String key);
}
