/**
 * 
 */
package net.ecology.auth.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.entity.auth.base.PrincipalDetails;
import net.ecology.exceptions.NgepAuthException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.model.Context;
import net.ecology.model.auth.UserAccountProfile;

/**
 * @author ducbq
 *
 */
public interface AuthorizationService {
	UserAccountProfile authenticate(String ssoId, String password) throws NgepAuthException;
	UserAccountProfile authenticate(String token) throws NgepAuthException;

	UserAccountProfile getActiveSecuredProfile() throws NgepAuthException;
	
	boolean hasPermission(String target, String action) throws NgepAuthException;

	UserPrincipal saveSecurityAccountProfile(UserPrincipal securityAccountProfile) throws NgepAuthException;

	UserAccountProfile register(Context context) throws NgepAuthException;

	UserPrincipal getUserAccount(String ssoId)  throws ObjectNotFoundException;

	UserAccountProfile confirmByToken(String token)  throws ObjectNotFoundException;

	List<AccessPolicy> getAccessDecisionPolicies(String accessPattern) throws ObjectNotFoundException;

	List<AccessPolicy> getAccessDecisionPolicies(PrincipalDetails authenticationDetails) throws ObjectNotFoundException;

	boolean hasAccessDecisionPolicy(FilterInvocation filterInvocation, Authentication authentication);
}
