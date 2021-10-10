/**
 * 
 */
package net.ecology.auth.core;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import net.ecology.auth.service.UserPrincipalService;
import net.ecology.common.CommonUtility;
import net.ecology.domain.auth.UserAccountProfile;
import net.ecology.entity.auth.GrantedPermission;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.exceptions.EcosExceptionCode;
import net.ecology.exceptions.AuthException;
import net.ecology.global.GlobeConstants;

/**
 * @author ducbq
 *
 */
public abstract class AuthorizationServiceBase {
	@Inject
	protected UserPrincipalService securityAccountService;

	protected UserAccountProfile generateSecurityPrincipalProfile(String authenticateToken, String password) throws AuthException {
		//UserAccountProfile userAccount = null;
		UserAccountProfile securityAccountProfile = null;

		UserPrincipal userPrincipal = null;
		if (CommonUtility.isEmpty(password)) {
			userPrincipal = securityAccountService.getUserAccount(authenticateToken);
			//userAccount = this.userAccountService.getUserAccount(authenticateToken);
		} else {
			userPrincipal = securityAccountService.getUserAccount(authenticateToken, password);
			//userAccount = this.userAccountService.getUserAccount(authenticateToken, password);
		}

		if (null==userPrincipal)
			throw new AuthException(EcosExceptionCode.ERROR_INVALID_PROFILE, "There is empty authentication user account");

		securityAccountProfile = UserAccountProfile.builder()
				.securityAccount(userPrincipal)
				//.userAccount(userAccount)
		.build();

		for (GrantedPermission userAccountPrivilege :userPrincipal.getGrantedAuthorities()) {
			securityAccountProfile.addGrantedAuthority(userAccountPrivilege.getAuthority());
		}

		return securityAccountProfile;
	}

	protected UserAccountProfile getCurrentSecuredProfile() throws AuthException {
		UserAccountProfile fetchedResponse = null;
		Object systemPrincipal = getSystemPrincipal();
		UserPrincipal userAccount = null;
		if (systemPrincipal instanceof User || systemPrincipal instanceof UserAccountProfile) {
			userAccount = this.securityAccountService.get(((User)systemPrincipal).getUsername());
			fetchedResponse = UserAccountProfile
			.builder()
			.displayName(new StringBuilder(userAccount.getContact().getFirstName()).append(GlobeConstants.STRING_SPACE).append(userAccount.getContact().getLastName()).toString())
			.securityAccount(userAccount)
			.build();
		} else if (systemPrincipal instanceof String){ //Anonymous user - Not logged in
			if (GlobeConstants.ANONYMOUS_USER.equalsIgnoreCase((String)systemPrincipal)) {
				fetchedResponse = UserAccountProfile.builder()
						.displayName((String)systemPrincipal)
						.build();
			} else {
				userAccount = this.securityAccountService.get((String)systemPrincipal);
				fetchedResponse = UserAccountProfile
				.builder()
				.displayName(new StringBuilder(userAccount.getContact().getFirstName()).append(GlobeConstants.STRING_SPACE).append(userAccount.getContact().getLastName()).toString())
				.securityAccount(userAccount)
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

}
