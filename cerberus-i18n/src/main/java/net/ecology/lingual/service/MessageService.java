/**
 * 
 */
package net.ecology.lingual.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;

import net.ecology.entity.i18n.I18nLocale;
import net.ecology.entity.i18n.Message;

/**
 * @author ducbq
 *
 */
public interface MessageService extends MessageSource {
	Map<String, String> getMessagesMap(Locale locale);
	List<Message> getMessages(Locale locale);
	void saveMessage(String key, String label, I18nLocale locale);
	void saveMessage(Message message);
	boolean exists(I18nLocale locale, String messageKey);
}
