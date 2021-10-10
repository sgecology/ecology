/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.general.LocalizedItem;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.SpecificationBasis;

/**
 * @author bqduc
 *
 */
@Builder
public class LocalizedItemSpecification extends SpecificationBasis<LocalizedItem, SearchSpec>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 267373264038078704L;

	public static Specification<LocalizedItem> buildSpecification(final SearchParameter searchParameter) {
		return LocalizedItemSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
