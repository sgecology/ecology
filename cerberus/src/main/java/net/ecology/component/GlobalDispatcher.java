/**
 * 
 */
package net.ecology.component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.ecology.auth.service.AuthorizationService;
import net.ecology.common.CommonUtility;
import net.ecology.entity.auth.UserAccountProfile;
import net.ecology.framework.component.BasisComp;
import net.ecology.global.GlobalConstants;

/**
 * @author ducbq
 *
 */
@Component
@Named(value = "globalDispatcher")
//@ViewScoped
@SessionScoped
public class GlobalDispatcher extends BasisComp {
	private static final long serialVersionUID = -4189926376687700775L;

	@Inject
	protected HttpSession httpSession;

	@Inject
	protected HttpServletRequest request;
	
	@Inject 
	protected ServletContext servletContext;
	
	@Inject
	private AuthorizationService authorizationService;

	private String failureMessage;

	public UserAccountProfile getSecurityAccountProfile() {
		UserAccountProfile securityAccountProfile = null;
		HttpSession session = null;
		try {
			session = session();
			if (null != session) {
				securityAccountProfile = (UserAccountProfile)session.getAttribute(GlobalConstants.AUTHENTICATED_PROFILE);
			}
		} catch (Exception e) {
			logger.error(e);
		}

		if (null == securityAccountProfile) {
			securityAccountProfile = authorizationService.getActiveSecuredProfile();
		}

		return securityAccountProfile;
	}
	
	protected HttpSession session() {
		this.httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		if (null != this.httpSession)
			return this.httpSession;

		if (null != this.request) {
			return this.request.getSession();
		}

		if (null != this.httpSession)
			return this.httpSession;

		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			this.httpSession = attr.getRequest().getSession(true);
		} catch (Exception e) {
			logger.error(e);
		}
		return this.httpSession;
		// return attr.getRequest().getSession(true); // true == allow create
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public String getUserProfileImageContentsAsBase64() {
		InputStream inputStream = null;
		byte[] imageBytes = null;
		UserAccountProfile securityPrincipalProfile = null;
		try {
			securityPrincipalProfile = this.getSecurityAccountProfile();
		} catch (Exception e) {
			logger.error(e);
		}

		if (null==securityPrincipalProfile || null == securityPrincipalProfile.getPicture()) {
			inputStream = servletContext.getResourceAsStream("/resources/images/8025287921598811056.png");
			try {
				imageBytes = CommonUtility.getByteArray(inputStream);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		} else {
			imageBytes = securityPrincipalProfile.getPicture();
		}
		return Base64.getEncoder().encodeToString(imageBytes);
	}
}
