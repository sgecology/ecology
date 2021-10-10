/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.general.CatalogueSubtype;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.SpecificationBasis;

/**
 * @author bqduc
 *
 */
@Builder
public class CatalogueSubtypeSpecification extends SpecificationBasis<CatalogueSubtype, SearchSpec>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2021774340255996625L;

	public static Specification<CatalogueSubtype> buildSpecification(final SearchParameter searchParameter) {
		return CatalogueSubtypeSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
