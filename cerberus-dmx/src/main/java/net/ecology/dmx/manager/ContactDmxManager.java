/**
 * 
 */
package net.ecology.dmx.manager;

import net.ecology.entity.contact.Contact;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.model.Context;
import net.ecology.xform.ContactMarshallable;
import net.ecology.xform.base.Marshallable;

/**
 * @author ducbq
 *
 */
public class ContactDmxManager extends ComponentRoot {
	private static final long serialVersionUID = -3935596472843057299L;

	public Context marshalling(Context context) {
		Marshallable<Contact, String[]> marshaller = ContactMarshallable.builder().build();
		marshaller.unmarshal(null);
		return context;
	}
}
