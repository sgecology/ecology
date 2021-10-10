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

import net.ecology.auth.certificate.TokenAuthenticationService;
import net.ecology.auth.core.AuthorizationServiceBase;
import net.ecology.auth.service.AccessPolicyService;
import net.ecology.auth.service.AuthorityService;
import net.ecology.auth.service.AuthorizationService;
import net.ecology.auth.service.UserPrincipalService;
import net.ecology.comm.comp.CommunicatorManager;
import net.ecology.comm.domain.MailMessage;
import net.ecology.comm.global.CommunicatorConstants;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.common.DateTimeUtility;
import net.ecology.domain.Context;
import net.ecology.domain.auth.UserAccountProfile;
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.entity.auth.base.PrincipalDetails;
import net.ecology.exceptions.AuthException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.global.GlobalConstants;

/**
 * @author ducbq
 *
 */
@Service
public class AuthorizationServiceImpl extends AuthorizationServiceBase implements AuthorizationService {
	@Inject 
	private CommunicatorManager communicatorHelper;

	@Inject
	private TokenAuthenticationService webTokenService;

	@Inject
	private AuthorityService authorityService;

	@Inject
	private AccessPolicyService accessDecisionPolicyService;
	
	@Inject
	private UserPrincipalService userPrincipalService;

	@Override
	public UserAccountProfile authenticate(String ssoId, String password) throws AuthException {
		return this.generateSecurityPrincipalProfile(ssoId, password);
	}

	@Override
	public UserAccountProfile authenticate(String loginToken) throws AuthException {
		return this.generateSecurityPrincipalProfile(loginToken, null);
	}

	@Override
	public UserAccountProfile getActiveSecuredProfile() throws AuthException {
		return this.getCurrentSecuredProfile();
	}

	@Override
	public boolean hasPermission(String target, String action) throws AuthException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserAccountProfile register(Context context) throws AuthException {
		String confirmLink = null;
		UserAccountProfile registrationProfile = null;
		MailMessage mailMessage = null;
		try {
			registrationProfile = userPrincipalService.register((UserPrincipal)context.get(CommunicatorConstants.CTX_USER_ACCOUNT));

			mailMessage = (MailMessage)context.get(CommunicatorConstants.CTX_MIME_MESSAGE);
			if (null==mailMessage) {
				mailMessage = MailMessage.builder()
						.subject(CommunicatorConstants.CTX_DEFAULT_REGISTRATION_SUBJECT)
						.recipients(new String[] {registrationProfile.getSecurityAccount().getEmail()})
						.build();
			}
			mailMessage.setRecipients(new String[] {registrationProfile.getSecurityAccount().getEmail()});
			mailMessage.getDefinitions().put(CommunicatorConstants.CTX_USER_TOKEN, registrationProfile.getSecurityAccount().getToken());

			confirmLink = (String)mailMessage.getDefinitions().get(GlobalConstants.CONFIG_APP_ACCESS_URL);
			mailMessage.getDefinitions().put(CommunicatorConstants.CTX_USER_CONFIRM_LINK, new StringBuilder(confirmLink).append(registrationProfile.getSecurityAccount().getToken()).toString());

			context.put(CommunicatorConstants.CTX_MIME_MESSAGE, mailMessage);

			communicatorHelper.sendEmail(mailMessage);
		} catch (Exception e) {
			throw new AuthException(e);
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
		PrincipalDetails userDetails = webTokenService.resolve(token);
		if (userDetails != null) {
			confirnUserAccount = this.getUserAccount(userDetails.getUsername());
		}

		confirnUserAccount.addPrivilege(authorityService.getMinimumUserAuthority());
		confirnUserAccount.setEnabled(Boolean.TRUE);
		confirnUserAccount.setVisible(Boolean.TRUE);
		confirnUserAccount.setEnabledDate(DateTimeUtility.systemDateTime());

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
		List<AccessPolicy> accessDecisionPolicies = CollectionsUtility.newList();
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
    	accessDecisionPolicies = CollectionsUtility.newList();
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
	public UserPrincipal saveSecurityAccountProfile(UserPrincipal securityAccountProfile) throws AuthException {
		if (CommonUtility.isEmpty(securityAccountProfile.getPassword())) {
			UserPrincipal verifySecurityAccountProfile = this.userPrincipalService.getObject(securityAccountProfile.getId());
			securityAccountProfile.setPassword(verifySecurityAccountProfile.getPassword());
		}
		this.securityAccountService.save(securityAccountProfile);
		return securityAccountProfile;
	}
}
