/**
 * 
 */
package net.ecology.auth.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import net.ecology.domain.Context;
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.UserAccountProfile;
import net.ecology.entity.base.UserAccountDetails;
import net.ecology.exceptions.AuthException;
import net.ecology.exceptions.ObjectNotFoundException;

/**
 * @author ducbq
 *
 */
public interface AuthorizationService {
	UserAccountProfile authenticate(String ssoId, String password) throws AuthException;
	UserAccountProfile authenticate(String token) throws AuthException;

	UserAccountProfile getActiveSecuredProfile() throws AuthException;
	
	boolean hasPermission(String target, String action) throws AuthException;

	UserAccountProfile saveSecurityAccountProfile(UserAccountProfile securityAccountProfile) throws AuthException;

	UserAccountProfile register(Context context) throws AuthException;

	UserAccountProfile getUserAccount(String ssoId)  throws ObjectNotFoundException;

	UserAccountProfile confirmByToken(String token)  throws ObjectNotFoundException;

	List<AccessPolicy> getAccessDecisionPolicies(String accessPattern) throws ObjectNotFoundException;

	List<AccessPolicy> getAccessDecisionPolicies(UserAccountDetails authenticationDetails) throws ObjectNotFoundException;

	boolean hasAccessDecisionPolicy(FilterInvocation filterInvocation, Authentication authentication);
}
