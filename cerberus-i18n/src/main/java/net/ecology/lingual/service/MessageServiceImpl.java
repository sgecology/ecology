/**
 * 
 */
package net.ecology.lingual.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.entity.i18n.I18nLocale;
import net.ecology.entity.i18n.Message;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.persistence.predicate.SearchCriteria;
import net.ecology.framework.persistence.predicate.SearchOperation;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.lingual.persistence.LocalePersistence;
import net.ecology.lingual.persistence.MessagePersistence;
import net.ecology.specification.MessageSpecification;

/**
 * @author ducbq
 *
 */
@Component("persistenceMessageSource")
public class MessageServiceImpl extends GenericService<Message, Long> implements MessageService {
	private static final long serialVersionUID = 149700141639581672L;

	@Inject
	private LocalePersistence localePersistence;

	@Inject
	private MessagePersistence labelPersistence;

	/**
	 * Try to resolve the message. Return default message if no message was found.
	 * @param code the message code to look up, e.g. 'calculator.noRateSet'.
	 * MessageSource users are encouraged to base message names on qualified class
	 * or package names, avoiding potential conflicts and ensuring maximum clarity.
	 * @param args an array of arguments that will be filled in for params within
	 * the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
	 * or {@code null} if none
	 * @param defaultMessage a default message to return if the lookup fails
	 * @param locale the locale in which to do the lookup
	 * @return the resolved message if the lookup was successful, otherwise
	 * the default message passed as a parameter (which may be {@code null})
	 * @see #getMessage(MessageSourceResolvable, Locale)
	 * @see java.text.MessageFormat
	 */
	@Override
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		return resolveMessage(code, args, locale);
	}

	/**
	 * Try to resolve the message. Treat as an error if the message can't be found.
	 * @param code the message code to look up, e.g. 'calculator.noRateSet'.
	 * MessageSource users are encouraged to base message names on qualified class
	 * or package names, avoiding potential conflicts and ensuring maximum clarity.
	 * @param args an array of arguments that will be filled in for params within
	 * the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
	 * or {@code null} if none
	 * @param locale the locale in which to do the lookup
	 * @return the resolved message (never {@code null})
	 * @throws NoSuchMessageException if no corresponding message was found
	 * @see #getMessage(MessageSourceResolvable, Locale)
	 * @see java.text.MessageFormat
	 */
	@Override
	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		return resolveMessage(code, args, locale);
	}

	/**
	 * Try to resolve the message using all the attributes contained within the
	 * {@code MessageSourceResolvable} argument that was passed in.
	 * <p>NOTE: We must throw a {@code NoSuchMessageException} on this method
	 * since at the time of calling this method we aren't able to determine if the
	 * {@code defaultMessage} property of the resolvable is {@code null} or not.
	 * @param resolvable the value object storing attributes required to resolve a message
	 * (may include a default message)
	 * @param locale the locale in which to do the lookup
	 * @return the resolved message (never {@code null} since even a
	 * {@code MessageSourceResolvable}-provided default message needs to be non-null)
	 * @throws NoSuchMessageException if no corresponding message was found
	 * (and no default message was provided by the {@code MessageSourceResolvable})
	 * @see MessageSourceResolvable#getCodes()
	 * @see MessageSourceResolvable#getArguments()
	 * @see MessageSourceResolvable#getDefaultMessage()
	 * @see java.text.MessageFormat
	 */
	@Override
	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		String message = null;
		for (String code : resolvable.getCodes()) {
			message = resolveMessage(code, resolvable.getArguments(), locale);
			if (message != null) {
				return message;
			}
		}
		return null;
	}

	private String resolveMessage(String code, Object[] args, Locale locale) {
		if (null==locale)
			return code;
		I18nLocale i18nLocale = localePersistence.findByLanguage(locale.getLanguage());
		Message messageSourceEntity = labelPersistence.findByKeyAndLocale(code, i18nLocale);
		String resolvedMessage = (null != messageSourceEntity)?messageSourceEntity.getLabel():code;
		if (CommonUtility.isNotEmpty(args)) {
			resolvedMessage = MessageFormat.format(messageSourceEntity.getLabel(), args);
		}
		return resolvedMessage; 
	}

	@Override
	public void saveMessage(String key, String label, I18nLocale locale) {
		Message messageEntity = this.labelPersistence.findByKeyAndLocale(key, locale);
		if (null==messageEntity) {
			messageEntity = Message.builder()
					.key(key)
					.label(label)
					.locale(locale)
					.build();
		} else {
			messageEntity.setLabel(label);
		}
		labelPersistence.save(messageEntity);
	}

	@Override
	public void saveMessage(Message message) {
		this.labelPersistence.saveAndFlush(message);
	}

	@Override
	public List<Message> getMessages(Locale locale) {
		I18nLocale i18nLocale = localePersistence.findByLanguage(locale.getLanguage());
		return this.labelPersistence.findByLocale(i18nLocale);
	}

	@Override
	public Map<String, String> getMessagesMap(Locale locale) {
		List<Message> messages = this.getMessages(locale);
		Map<String, String> messagesMap = CollectionsUtility.newMap();
		for (Message mse :messages) {
			messagesMap.put(mse.getKey(), mse.getLabel());
		}
		return messagesMap;
	}

	@Override
	public boolean exists(I18nLocale locale, String messageKey) {
		MessageSpecification messageSpecification = (MessageSpecification)MessageSpecification.builder().build()
			.add(new SearchCriteria("key", messageKey, SearchOperation.EQUAL))
			.add(new SearchCriteria("locale", locale, SearchOperation.EQUAL));

		long counter = this.labelPersistence.count(messageSpecification);
		return (counter > 0);
	}

	@Override
	protected IPersistence<Message, Long> getPersistence() {
		return this.labelPersistence;
	}
}
