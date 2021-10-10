/**
 * 
 */
package net.ecology.specification;

import lombok.Builder;
import net.ecology.entity.i18n.Message;
import net.ecology.framework.persistence.predicate.GenericSpecification;

/**
 * @author bqduc
 *
 */
@Builder
public class MessageSpecification extends GenericSpecification <Message>{
	private static final long serialVersionUID = 2555760303776463939L;
}
