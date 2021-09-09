/**
 * 
 */
package net.ecology.framework.predicator;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.framework.model.SearchParameter;

/**
 * @author bqduc
 *
 */
@Builder
public class DefaultBrilliancePredicator<T> extends BrilliancePredicator<T>{
	public Specification<T> buildSpecification(final SearchParameter searchParameter){
		return predicateSpecification(searchParameter);
	}
}
