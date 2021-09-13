/**
 * 
 */
package net.ecology.auth.intercept;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import net.ecology.auth.service.AccessPolicyService;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.global.GlobeConstants;
import net.ecology.model.auth.AccessDecision;

/**
 * @author ducbq
 * Identity and Access Management
 */
@Component
public class IdentityAccessManager {
	@Inject
	private AccessPolicyService accessPolicyService;

	public boolean decide(HttpServletRequest request, Authentication authentication) {
		return this.resolve(request, authentication);
	}

	@SuppressWarnings("unchecked")
  private boolean resolve(HttpServletRequest request, Authentication authentication) {
		if (authentication == null || GlobeConstants.ANONYMOUS_USER.equals(authentication.getPrincipal())) {
			return false;
		}

		final String MY_ACCESSED_DECISION_POLICIES = "myAccessedDecisionPolicies";
		boolean hasAccessedPermission = false;
		List<AccessPolicy> accessDecisionPolicies = null;
		List<AccessPolicy> currentADPs = null; 
		PathMatcher pathMatcher = null;

		accessDecisionPolicies = (List<AccessPolicy>)request.getSession(false).getAttribute(MY_ACCESSED_DECISION_POLICIES);
    if (CommonUtility.isEmpty(accessDecisionPolicies)) {
    	accessDecisionPolicies = CollectionsUtility.newList();
  		for (GrantedAuthority authority :authentication.getAuthorities()) {
  			currentADPs = accessPolicyService.getAccessPoliciesByAuthority((Authority)authority);
  			if (!currentADPs.isEmpty()) {
  	  		for (AccessPolicy accessDecisionPolicy :currentADPs) {
  	  			if (AccessDecision.ACCESS_GRANTED.equals(accessDecisionPolicy.getAccessDecision())) {
  	  				accessDecisionPolicies.add(accessDecisionPolicy);
  	  			}
  	  		}
  			}
  		}

  		request.getSession(false).setAttribute(MY_ACCESSED_DECISION_POLICIES, accessDecisionPolicies);
    }

		pathMatcher = new AntPathMatcher();
		for (AccessPolicy accessDecisionPolicy :accessDecisionPolicies) {
			if (pathMatcher.match(accessDecisionPolicy.getAccessPattern(), request.getRequestURI())) {
				hasAccessedPermission = true;
			}
		}
		return hasAccessedPermission;
  }
}
