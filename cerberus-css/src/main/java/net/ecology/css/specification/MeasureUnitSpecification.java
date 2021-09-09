/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.general.MeasureUnit;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class MeasureUnitSpecification extends CoreSpecifications<MeasureUnit, SearchSpec>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7455339506976096038L;

	public static Specification<MeasureUnit> buildSpecification(final SearchParameter searchParameter) {
		return MeasureUnitSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
