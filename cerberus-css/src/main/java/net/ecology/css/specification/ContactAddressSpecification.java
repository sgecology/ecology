/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.contact.ContactAddress;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class ContactAddressSpecification extends CoreSpecifications <ContactAddress, SearchSpec>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4984760941753997036L;

	public static Specification<ContactAddress> buildSpecification(final SearchParameter searchParameter) {
		return ContactAddressSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
