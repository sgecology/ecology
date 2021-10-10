/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.system.Option;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.SpecificationBasis;

/**
 * @author bqduc
 *
 */
@Builder
public class OptionSpecification extends SpecificationBasis<Option, SearchSpec>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3900233288875704002L;

	public static Specification<Option> buildSpecification(final SearchParameter searchParameter) {
		return OptionSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
