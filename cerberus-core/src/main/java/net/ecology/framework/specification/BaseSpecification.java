/**
 * 
 */
package net.ecology.framework.specification;

import org.springframework.data.jpa.domain.Specification;

import net.ecology.common.CommonUtility;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.framework.model.SearchParameter;

/**
 * @author bqduc
 */
public abstract class BaseSpecification<UserType, UserRequest> extends ComponentRoot {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7747799070916985450L;

	public abstract Specification<UserType> getFilter(UserRequest request);

	protected String containsLowerCase(String searchField) {
		return new StringBuilder()
				.append(CommonUtility.STRING_WILDCARD).append(CommonUtility.getApplicableString(searchField)).append(CommonUtility.STRING_WILDCARD)
				.toString();
	}

	protected String containsWildcard(String searchField) {
		if (CommonUtility.isEmpty(searchField))
			return CommonUtility.STRING_BLANK;

		return CommonUtility.STRING_WILDCARD + searchField + CommonUtility.STRING_WILDCARD;
	}

	protected abstract Specification<UserType> buildSpecifications(final SearchParameter searchParameter);
}