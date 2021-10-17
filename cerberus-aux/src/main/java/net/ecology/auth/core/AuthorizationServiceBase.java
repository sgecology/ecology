/**
 * 
 */
package net.ecology.auth.core;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import net.ecology.auth.service.AccessPolicyService;
import net.ecology.auth.service.UserPrincipalService;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.domain.AccessDecision;
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.auth.GrantedPermission;
import net.ecology.entity.auth.UserAccountProfile;
import net.ecology.exceptions.AuthException;
import net.ecology.exceptions.EcosExceptionCode;
import net.ecology.global.GlobeConstants;

/**
 * @author ducbq
 *
 */
public abstract class AuthorizationServiceBase {
	@Inject
	protected UserPrincipalService securityAccountService;

	@Inject
	protected AccessPolicyService accessPolicyService;

	protected UserAccountProfile authenticatePrincipalProfile(String authenticateToken, String password) throws AuthException {
		UserAccountProfile securityAccountProfile = null;

		UserAccountProfile userPrincipal = null;
		if (CommonUtility.isEmpty(password)) {
			userPrincipal = securityAccountService.getUserAccount(authenticateToken);
		} else {
			userPrincipal = securityAccountService.getUserAccount(authenticateToken, password);
		}

		if (null==userPrincipal)
			throw new AuthException(EcosExceptionCode.ERROR_INVALID_PROFILE, "There is empty authentication user account");

		securityAccountProfile = UserAccountProfile.builder()
				//.securityAccount(userPrincipal)
				//.userAccount(userAccount)
		.build();

		for (GrantedPermission userAccountPrivilege :userPrincipal.getGrantedAuthorities()) {
			securityAccountProfile.addPrivilege(userAccountPrivilege.getAuthority());
		}

		securityAccountProfile.setAccessPolicies(getAccessPolicies(userPrincipal));
		return securityAccountProfile;
	}

	protected UserAccountProfile getCurrentSecuredProfile() throws AuthException {
		UserAccountProfile fetchedResponse = null;
		Object systemPrincipal = getSystemPrincipal();
		UserAccountProfile userAccount = null;
		if (systemPrincipal instanceof User || systemPrincipal instanceof UserAccountProfile) {
			userAccount = this.securityAccountService.get(((User)systemPrincipal).getUsername());
			fetchedResponse = UserAccountProfile
			.builder()
			.firstName(userAccount.getFirstName())
			.lastName(userAccount.getLastName())
			//.securityAccount(userAccount)
			.build();
		} else if (systemPrincipal instanceof String){ //Anonymous user - Not logged in
			if (GlobeConstants.ANONYMOUS_USER.equalsIgnoreCase((String)systemPrincipal)) {
				fetchedResponse = UserAccountProfile.builder()
						.firstName((String)systemPrincipal)
						.build();
			} else {
				userAccount = this.securityAccountService.get((String)systemPrincipal);
				fetchedResponse = UserAccountProfile
				.builder()
				.firstName(userAccount.getFirstName())
				.lastName(userAccount.getLastName())
				//.securityAccount(userAccount)
				.build();
			}
		}
		return fetchedResponse;
	}

	protected Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication;
	}

	protected Object getAuthenticationPrincipal() {
		Authentication authentication = this.getAuthentication();
		return (null != authentication)? authentication.getPrincipal():null;
	}

	protected Object getSystemPrincipal() {
		Authentication authentication = this.getAuthentication();
		if (null==authentication)
			return null;

		if (authentication.getPrincipal() instanceof String) {
			return authentication.getPrincipal();
		}

		return authentication.getPrincipal();
	}

	private List<AccessPolicy> getAccessPolicies(UserAccountProfile userPrincipal){
		List<AccessPolicy> currentAccessPolicies = null; 
		List<AccessPolicy> myAccessPolicies = CollectionsUtility.newList();
		for (GrantedAuthority authority :userPrincipal.getAuthorities()) {
			currentAccessPolicies = accessPolicyService.getAccessPoliciesByAuthority((Authority)authority);
			if (currentAccessPolicies.isEmpty()) 
				continue;
			
  		for (AccessPolicy accessDecisionPolicy :currentAccessPolicies) {
  			if (AccessDecision.ACCESS_GRANTED.equals(accessDecisionPolicy.getAccessDecision())) {
  				myAccessPolicies.add(accessDecisionPolicy);
  			}
  		}
		}
		return myAccessPolicies;
	}
}
