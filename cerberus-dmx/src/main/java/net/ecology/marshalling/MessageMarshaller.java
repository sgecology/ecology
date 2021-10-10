/**
 * 
 */
package net.ecology.marshalling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.entity.i18n.Message;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class MessageMarshaller extends MarshallerBasis<Message, String[]> {
	public static short idx_locale = 0;
	public static short idx_key = 1;
	public static short idx_label = 2;

	@Override
	public Message unmarshal(String[] elements) {
		return Message.builder()
    //.language(elements[idx_locale])
    .key(elements[idx_key])
    .label(elements[idx_label])
    .build();
	}
}
