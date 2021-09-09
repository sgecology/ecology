package net.ecology.auth.intercept;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import net.ecology.auth.service.AuthorizationService;
import net.ecology.common.CommonUtility;
import net.ecology.exceptions.NgepAuthException;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.global.GlobalConstants;
import net.ecology.model.auth.UserAccountProfile;

/**
 * Created by aLeXcBa1990 on 24/11/2018.
 * 
 */
@Component(value="authenticationProvider")
public class CustomAuthenticationProvider extends ComponentRoot implements AuthenticationProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8355652678792800184L;

	@Inject
	ObjectFactory<HttpSession> httpSessionFactory;
	
	@Inject 
	private AuthorizationService authorizationService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication authenticateResp = null;
		try {
			if (authentication.getName().length() < 150) {
				authenticateResp = authenticateBySsoId(authentication.getName(), authentication.getCredentials().toString());
			} else {
				authenticateResp = authenticateByToken(authentication.getName());
			}
		} catch (NgepAuthException cae) {
			logger.error(cae);
			throw cae;
		}
		return authenticateResp;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	private Authentication authenticateBySsoId(String ssoId, String password) throws NgepAuthException {
		Authentication authObject = null;
		UserAccountProfile securityPrincipalProfile = null;
		try {
			securityPrincipalProfile = authorizationService.authenticate(ssoId, password);
			if (null != securityPrincipalProfile) {
				authObject = new UsernamePasswordAuthenticationToken(ssoId, password, securityPrincipalProfile.getSecurityAccount().getAuthorities());
				securityPrincipalProfile.setAuthentication(authObject);

				httpSessionFactory.getObject().setAttribute(GlobalConstants.AUTHENTICATED_PROFILE, securityPrincipalProfile);
			}
		} catch (Exception uae) {
			throw new NgepAuthException(uae);
		}

		return authObject;
	}

	private Authentication authenticateByToken(String token) throws NgepAuthException {
		Authentication authByToken = null;
		UserAccountProfile userAccountProfile = null;
		try {
			userAccountProfile = authorizationService.authenticate(token);
			if (null != userAccountProfile) {
				authByToken = new UsernamePasswordAuthenticationToken(token, CommonUtility.STRING_BLANK, userAccountProfile.getSecurityAccount().getAuthorities());			
			}
		} catch (Exception e) {
			throw new NgepAuthException(e);
		}

		return authByToken;
	}
}
