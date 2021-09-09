/**
 * 
 */
package net.ecology.auth.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import net.ecology.auth.comp.JWTServiceImpl;
import net.ecology.auth.core.AuthorizationServiceBase;
import net.ecology.auth.service.AccessPolicyService;
import net.ecology.auth.service.AuthorityService;
import net.ecology.auth.service.AuthorizationService;
import net.ecology.auth.service.UserPrincipalService;
import net.ecology.comm.comp.Communicator;
import net.ecology.comm.domain.CorpMimeMessage;
import net.ecology.comm.global.CommunicatorConstants;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.common.DateTimeUtility;
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.entity.auth.base.PrincipalDetails;
import net.ecology.exceptions.NgepAuthException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.global.GlobalConstants;
import net.ecology.model.Context;
import net.ecology.model.auth.UserAccountProfile;

/**
 * @author ducbq
 *
 */
@Service
public class AuthorizationServiceImpl extends AuthorizationServiceBase implements AuthorizationService {
	@Inject
	private Communicator emailCommunicator;

	@Inject
	private JWTServiceImpl tokenProvider;

	@Inject
	private AuthorityService authorityService;

	@Inject
	private AccessPolicyService accessDecisionPolicyService;
	
	@Inject
	private UserPrincipalService userPrincipalService;

	@Override
	public UserAccountProfile authenticate(String ssoId, String password) throws NgepAuthException {
		return this.generateSecurityPrincipalProfile(ssoId, password);
	}

	@Override
	public UserAccountProfile authenticate(String loginToken) throws NgepAuthException {
		return this.generateSecurityPrincipalProfile(loginToken, null);
	}

	@Override
	public UserAccountProfile getActiveSecuredProfile() throws NgepAuthException {
		return this.getCurrentSecuredProfile();
	}

	@Override
	public boolean hasPermission(String target, String action) throws NgepAuthException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserAccountProfile register(Context context) throws NgepAuthException {
		String confirmLink = null;
		UserAccountProfile registrationProfile = null;
		CorpMimeMessage mimeMessage = null;
		try {
			registrationProfile = userPrincipalService.register((UserPrincipal)context.get(CommunicatorConstants.CTX_USER_ACCOUNT));

			mimeMessage = (CorpMimeMessage)context.get(CommunicatorConstants.CTX_MIME_MESSAGE);
			if (null==mimeMessage) {
				mimeMessage = CorpMimeMessage.builder()
						.subject(CommunicatorConstants.CTX_DEFAULT_REGISTRATION_SUBJECT)
						.recipients(new String[] {registrationProfile.getSecurityAccount().getEmail()})
						.build();
			}
			mimeMessage.setRecipients(new String[] {registrationProfile.getSecurityAccount().getEmail()});
			mimeMessage.getDefinitions().put(CommunicatorConstants.CTX_USER_TOKEN, registrationProfile.getSecurityAccount().getToken());

			confirmLink = (String)mimeMessage.getDefinitions().get(GlobalConstants.CONFIG_APP_ACCESS_URL);
			mimeMessage.getDefinitions().put(CommunicatorConstants.CTX_USER_CONFIRM_LINK, new StringBuilder(confirmLink).append(registrationProfile.getSecurityAccount().getToken()).toString());

			context.put(CommunicatorConstants.CTX_MIME_MESSAGE, mimeMessage);

			emailCommunicator.send(context);
		} catch (Exception e) {
			throw new NgepAuthException(e);
		}
		return registrationProfile;
	}

	@Override
	public UserPrincipal getUserAccount(String ssoId) throws ObjectNotFoundException {
		return securityAccountService.get(ssoId);
	}

	@Override
	public UserAccountProfile confirmByToken(String token) throws ObjectNotFoundException {
		UserAccountProfile confirmedSecurityAccountProfile = UserAccountProfile.builder().build();
		UserPrincipal confirnUserAccount = null;
		PrincipalDetails userDetails = tokenProvider.generateAuthenticationDetails(token);
		if (userDetails != null) {
			confirnUserAccount = this.getUserAccount(userDetails.getUsername());
		}

		confirnUserAccount.addPrivilege(authorityService.getMinimumUserAuthority());
		confirnUserAccount.setEnabled(Boolean.TRUE);
		confirnUserAccount.setVisible(Boolean.TRUE);
		confirnUserAccount.setEnabledDate(DateTimeUtility.getSystemDateTime());

		securityAccountService.save(confirnUserAccount);
		confirmedSecurityAccountProfile.setSecurityAccount(confirnUserAccount);
		return confirmedSecurityAccountProfile;
	}

	@Override
	public List<AccessPolicy> getAccessDecisionPolicies(String accessPattern) throws ObjectNotFoundException {
		return accessDecisionPolicyService.getAccessPolicies(accessPattern);
	}

	@Override
	public List<AccessPolicy> getAccessDecisionPolicies(PrincipalDetails authenticationDetails) throws ObjectNotFoundException {
		List<AccessPolicy> accessDecisionPolicies = CollectionsUtility.createDataList();
		List<AccessPolicy> currentADPs = null; 
		for (GrantedAuthority authority :authenticationDetails.getAuthorities()) {
			currentADPs = accessDecisionPolicyService.getAccessPoliciesByAuthority((Authority)authority);
			if (!currentADPs.isEmpty()) {
				accessDecisionPolicies.addAll(currentADPs);
			}
		}
		return accessDecisionPolicies;
	}

	@Override
	public boolean hasAccessDecisionPolicy(FilterInvocation filterInvocation, Authentication authentication) {
		final String MY_ACCESSED_DECISION_POLICIES = "myAccessedDecisionPolicies";
		boolean hasAccessedPermission = false;
		List<AccessPolicy> accessDecisionPolicies = null;
		List<AccessPolicy> currentADPs = null; 
		PathMatcher pathMatcher = null;

		accessDecisionPolicies = (List<AccessPolicy>)filterInvocation.getHttpRequest().getSession(false).getAttribute(MY_ACCESSED_DECISION_POLICIES);
    if (null==accessDecisionPolicies) {
    	accessDecisionPolicies = CollectionsUtility.createDataList();
  		for (GrantedAuthority authority :authentication.getAuthorities()) {
  			currentADPs = accessDecisionPolicyService.getAccessPoliciesByAuthority((Authority)authority);
  			if (!currentADPs.isEmpty()) {
  				accessDecisionPolicies.addAll(currentADPs);
  			}
  		}

  		filterInvocation.getHttpRequest().getSession(false).setAttribute(MY_ACCESSED_DECISION_POLICIES, accessDecisionPolicies);
    }

		pathMatcher = new AntPathMatcher();
		for (AccessPolicy accessDecisionPolicy :accessDecisionPolicies) {
			if (pathMatcher.match(accessDecisionPolicy.getAccessPattern(), filterInvocation.getRequestUrl())) {
				hasAccessedPermission = true;
			}
		}

		return hasAccessedPermission;
	}

	@Override
	public UserPrincipal saveSecurityAccountProfile(UserPrincipal securityAccountProfile) throws NgepAuthException {
		if (CommonUtility.isEmpty(securityAccountProfile.getPassword())) {
			UserPrincipal verifySecurityAccountProfile = this.userPrincipalService.getObject(securityAccountProfile.getId());
			securityAccountProfile.setPassword(verifySecurityAccountProfile.getPassword());
		}
		this.securityAccountService.save(securityAccountProfile);
		return securityAccountProfile;
	}
}
