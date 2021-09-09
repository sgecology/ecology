/**
 * 
 */
package net.ecology.config.security;

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
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.global.GlobeConstants;
import net.ecology.model.auth.AccessDecision;

/**
 * @author ducbq
 *
 */
@Component
public class AuthorizationChecker {
	
	@Inject
	private AccessPolicyService accessDecisionPolicyService;

	@SuppressWarnings("unchecked")
  private boolean doCheck(HttpServletRequest request, Authentication authentication) {
		if (authentication == null || GlobeConstants.ANONYMOUS_USER.equals(authentication.getPrincipal())) {
			return false;
		}

		final String MY_ACCESSED_DECISION_POLICIES = "myAccessedDecisionPolicies";
		boolean hasAccessedPermission = false;
		List<AccessPolicy> accessDecisionPolicies = null;
		List<AccessPolicy> currentADPs = null; 
		PathMatcher pathMatcher = null;

		accessDecisionPolicies = (List<AccessPolicy>)request.getSession(false).getAttribute(MY_ACCESSED_DECISION_POLICIES);
    if (null==accessDecisionPolicies) {
    	accessDecisionPolicies = CollectionsUtility.createDataList();
  		for (GrantedAuthority authority :authentication.getAuthorities()) {
  			currentADPs = accessDecisionPolicyService.getAccessPoliciesByAuthority((Authority)authority);
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

	public boolean decide(HttpServletRequest request, Authentication authentication) {
		return this.doCheck(request, authentication);
	}

	/*
	public boolean check(HttpServletRequest request, Authentication authentication) {
		Object principalObj = authentication.getPrincipal();

		if (!(principalObj instanceof User)) {
			return false;
		}

		String authority = null;
		for (SecurityUrlMatcher matcher : urlMatcherRepository.findAll()) {
			if (new AntPathMatcher().match(matcher.getUrl(), request.getRequestURI())) {
				authority = matcher.getAuthority();
				break;
			}
		}

		String userId = ((User) authentication.getPrincipal()).getUserId();
		User loggedUser = userRepository.findByUserId(userId);

		List<String> authorities = loggedUser.getAuthority();

		if (authority == null || !authorities.contains(authority)) {
			return false;
		}
		return true;
	}*/
}
