/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.general.Catalogue;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class CatalogueSpecification extends CoreSpecifications<Catalogue, SearchSpec>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2021774340255996625L;

	public static Specification<Catalogue> buildSpecification(final SearchParameter searchParameter) {
		return CatalogueSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
