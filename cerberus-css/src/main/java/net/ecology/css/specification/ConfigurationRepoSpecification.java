/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.config.Configuration;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.SpecificationBasis;

/**
 * @author bqduc
 *
 */
@Builder
public class ConfigurationRepoSpecification extends SpecificationBasis<Configuration, SearchSpec>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4115991455691287486L;

	public static Specification<Configuration> buildSpecification(final SearchParameter searchParameter) {
		return ConfigurationRepoSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
