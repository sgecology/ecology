/**
 * 
 */
package net.ecology.auth.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.auth.Authority;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.SpecificationBasis;

/**
 * @author bqduc
 *
 */
@Builder
public class AuthoritySpecification extends SpecificationBasis<Authority, SearchSpec>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2021774340255996625L;

	public static Specification<Authority> buildSpecification(final SearchParameter searchParameter) {
		return AuthoritySpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
