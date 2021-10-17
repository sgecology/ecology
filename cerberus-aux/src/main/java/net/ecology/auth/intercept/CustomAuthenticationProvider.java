package net.ecology.auth.intercept;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import net.ecology.auth.constants.GlobeAuxConstants;
import net.ecology.auth.service.AccessPolicyService;
import net.ecology.auth.service.AuthorizationService;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.domain.AccessDecision;
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.auth.TokenizeAuthentication;
import net.ecology.entity.auth.UserAccountProfile;
import net.ecology.exceptions.AuthException;
import net.ecology.framework.component.BasisComp;
import net.ecology.global.GlobalConstants;

/**
 * Created by aLeXcBa1990 on 24/11/2018.
 * 
 */
@Component(value="authenticationProvider")
public class CustomAuthenticationProvider extends BasisComp implements AuthenticationProvider {
	private static final long serialVersionUID = -2315026041215148528L;

	@Inject
	private ObjectFactory<HttpSession> httpSessionFactory;

	@Inject 
	private AuthorizationService authorizationService;

	@Inject
	private AccessPolicyService accessPolicyService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication authenticateResp = null;
		try {
			if (authentication.getName().length() < 150) {
				authenticateResp = authenticateBySsoId(authentication.getName(), authentication.getCredentials().toString());
			} else {
				authenticateResp = authenticateByToken(authentication.getName());
			}
		} catch (AuthException cae) {
			logger.error(cae);
			throw cae;
		}
		return authenticateResp;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	private Authentication authenticateBySsoId(String ssoId, String password) throws AuthException {
		Authentication authObject = null;
		UserAccountProfile userAccountProfile = null;
		try {
			userAccountProfile = authorizationService.authenticate(ssoId, password);
			if (null != userAccountProfile) {
				authObject = new TokenizeAuthentication(ssoId, password, userAccountProfile.getAuthorities(), userAccountProfile.getAccessPolicies());
				userAccountProfile.setAuthentication(authObject);

				httpSessionFactory.getObject().setAttribute(GlobalConstants.AUTHENTICATED_PROFILE, userAccountProfile);
			}
		} catch (Exception uae) {
			throw new AuthException(uae);
		}

		return authObject;
	}

	private Authentication authenticateByToken(String token) throws AuthException {
		Authentication authByToken = null;
		UserAccountProfile userAccountProfile = null;
		try {
			userAccountProfile = authorizationService.authenticate(token);
			if (null != userAccountProfile) {
				authByToken = new TokenizeAuthentication(token, CommonUtility.STRING_BLANK, userAccountProfile.getAuthorities(), userAccountProfile.getAccessPolicies());
			}
		} catch (Exception e) {
			throw new AuthException(e);
		}

		return authByToken;
	}

	private List<AccessPolicy> getMyAccessPolicies(Authentication authentication){
		List<AccessPolicy> currentAccessPolicies = null; 
		List<AccessPolicy> myAccessPolicies = CollectionsUtility.newList();
		for (GrantedAuthority authority :authentication.getAuthorities()) {
			currentAccessPolicies = accessPolicyService.getAccessPoliciesByAuthority((Authority)authority);
			if (!currentAccessPolicies.isEmpty()) {
	  		for (AccessPolicy accessDecisionPolicy :currentAccessPolicies) {
	  			if (AccessDecision.ACCESS_GRANTED.equals(accessDecisionPolicy.getAccessDecision())) {
	  				myAccessPolicies.add(accessDecisionPolicy);
	  			}
	  		}
			}
		}

		this.httpSessionFactory.getObject().setAttribute(GlobeAuxConstants.ACCESSED_POLICIES, myAccessPolicies);
		return myAccessPolicies;
	}
}