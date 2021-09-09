/**
 * 
 */
package net.ecology.framework.persistence.predicate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class SearchCriteria {
	@Setter @Getter
	private String key;

	@Setter @Getter
	private Object value;

	@Setter @Getter
	private SearchOperation operation;
}
