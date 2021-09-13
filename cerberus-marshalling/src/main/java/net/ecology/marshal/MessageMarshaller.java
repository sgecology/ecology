/**
 * 
 */
package net.ecology.marshal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.base.Marshaller;
import net.ecology.entity.i18n.Message;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class MessageMarshaller implements Marshaller<Message, String[]> {
	public static short idx_locale = 0;
	public static short idx_key = 1;
	public static short idx_label = 2;

	@Override
	public Message marshal(String[] elements) {
		return Message.builder()
    //.language(elements[idx_locale])
    .key(elements[idx_key])
    .label(elements[idx_label])
    .build();
	}

	@Override
	public String[] unmarshal(Message repoObject) {
		// TODO Auto-generated method stub
		return null;
	}

}
