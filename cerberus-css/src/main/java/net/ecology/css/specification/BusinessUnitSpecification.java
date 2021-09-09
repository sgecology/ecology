/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.business.BusinessUnit;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class BusinessUnitSpecification extends CoreSpecifications <BusinessUnit, SearchSpec>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5864251508969605374L;

	public static Specification<BusinessUnit> buildSpecification(final SearchParameter searchParameter) {
		return BusinessUnitSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
