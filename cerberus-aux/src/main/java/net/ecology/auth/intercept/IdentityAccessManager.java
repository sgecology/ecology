/**
 * 
 */
package net.ecology.auth.intercept;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.TokenizeAuthentication;
import net.ecology.global.GlobeConstants;

/**
 * @author ducbq
 * Identity and Access Management
 */
@Component
public class IdentityAccessManager {
	//@Inject
	//private AccessPolicyService accessPolicyService;

	public boolean decide(HttpServletRequest request, Authentication authentication) {
		return this.resolve(request, authentication);
	}

  private boolean resolve(HttpServletRequest request, Authentication authentication) {
		if (authentication == null || GlobeConstants.ANONYMOUS_USER.equals(authentication.getPrincipal())) {
			return false;
		}

		//final String MY_ACCESSED_DECISION_POLICIES = "myAccessedDecisionPolicies";
		boolean hasAccessedPermission = false;
		List<AccessPolicy> accessDecisionPolicies = ((TokenizeAuthentication)authentication).getAccessPolicies();
		PathMatcher pathMatcher = new AntPathMatcher();
		for (AccessPolicy accessDecisionPolicy :accessDecisionPolicies) {
			if (pathMatcher.match(accessDecisionPolicy.getAccessPattern(), request.getRequestURI())) {
				hasAccessedPermission = true;
				break;
			}
		}
		return hasAccessedPermission;
  }
}
