/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.contact.Contact;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.SpecificationBasis;

/**
 * @author bqduc
 *
 */
@Builder
public class ContactSpecification extends SpecificationBasis<Contact, SearchSpec>{
	private static final long serialVersionUID = -4351535957683794972L;

	public static Specification<Contact> buildSpecification(final SearchParameter searchParameter) {
		return ContactSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
