/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecology.controller.common;

import java.io.IOException;
import java.util.Base64;

import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;

import lombok.Getter;
import lombok.Setter;
import net.ecology.auth.service.AuthorizationService;
import net.ecology.auth.service.UserPrincipalService;
import net.ecology.common.CommonConstants;
import net.ecology.common.CommonUtility;
import net.ecology.domain.Context;
import net.ecology.domain.auth.UserAccountProfile;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.entity.business.BusinessUnit;
import net.ecology.exceptions.AccessDeniedException;
import net.ecology.framework.controller.DetailHome;
import net.ecology.global.GlobalConstants;
import net.ecology.global.GlobeConstants;
import net.ecology.repository.GlobalDataRepository;

/**
 * @author ducbq
 */
@Named
@FlowScoped("masterLandingHome")
public class MasterLandingHome extends DetailHome<UserAccountProfile> {
	private static final long serialVersionUID = 3011945566126846163L;

	@Inject
	private UserPrincipalService businessService;

	@Inject 
	private AuthorizationService authorizationService;

	@Inject 
	private GlobalDataRepository globalDataRepository;
	/*	@Inject 
	private ConfigurationService configurationService;*/

	private Long id;

	private BusinessUnit businessUnit;
	private UserPrincipal entity;

	@Setter
	@Getter
	private UploadedFile uploadedFile;

	private UserAccountProfile getUserProfile() {
		UserAccountProfile securityAccountProfile = null;
		try {
			securityAccountProfile = (UserAccountProfile)this.httpSession.getAttribute(GlobalConstants.AUTHENTICATED_PROFILE);
		} catch (Exception e) {
			logger.error(e);
		}

		if (null == securityAccountProfile) {
			securityAccountProfile = authorizationService.getActiveSecuredProfile();
		}
		
		return securityAccountProfile;
	}

	public void init() {
		if (Faces.isAjaxRequest()) {
			return;
		}

		Long userId = null;
		if (CommonUtility.isNotEmpty(super.request.getParameter("id"))){
			userId = Long.valueOf(super.request.getParameter("id"));
			this.entity = businessService.getObject(userId);

			UserAccountProfile securityPrincipalProfile = this.getUserProfile();
			if (null == securityPrincipalProfile || null == securityPrincipalProfile.getSecurityAccount() || !userId.equals(securityPrincipalProfile.getSecurityAccount().getId())) {
				//////////////////// Leak
				logger.info("Illegal access. ");
				return;
			}
		} else {
			this.entity = UserPrincipal.builder().build();
		}
	}

	public void remove() throws IOException {
		if (!facesService.isUserInRole("ROLE_ADMIN")) {
			throw new AccessDeniedException("User not authorized! Only role <b>admin</b> can remove cars.");
		}
		if (null != entity && null != entity.getId()) {
			//businessService.remove(entity);
			facesService.addDetailMessage("Car " + entity.getUsername() + " removed successfully");
			Faces.getFlash().setKeepMessages(true);
			//Faces.redirect("user/car-list.jsf");
		}
	}

	public void registerProfile(String language) {
		try {
			preProcessUserAccount();
			/*
			if (!this.validate()) {
				utils.addDetailMessage(persistenceMessageSource.getMessage("msg.userAccountRegisterFailure", new Object[] {this.entity.getEmail()}, super.getCurrentLocale()));
				Faces.getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "register");
				return;
			}
			*/
/*
			if (this.businessUnit != null) {
				this.entity.setCompanyName(this.businessUnit.getName());
			}
			
			this.entity.setStateProvince("Sài Gòn");

			this.entity.setBusinessUnitCode(this.businessUnit.getCode());

			Context context = Context.builder().build();
			
			context.put(CommunicatorConstants.CTX_MAIL_TEMPLATE_DIR, "/template/");
			context.put(CommunicatorConstants.CTX_MAIL_TEMPLATE_ID, "/auth/register.ftl");
			context.put(CommunicatorConstants.CTX_USER_ACCOUNT, this.entity);
			this.buildRegistrationContext(context);

			this.authorizationService.register(context);
			//businessService.registerUserAccount(this.entity);
			//facesService.addDetailMessage(persistenceMessageSource.getMessage("msg.userAccountRegisterSuccess", new Object[] {this.entity.getEmail()}, super.getCurrentLocale()));
			Faces.getFlash().setKeepMessages(true);
			FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index");
			*/
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void updateProfile() {
		try {
			preProcessUserAccount();
			this.authorizationService.saveSecurityAccountProfile(this.entity);

			//Synchronize back to session
			UserAccountProfile securityPrincipalProfile = (UserAccountProfile)this.httpSession.getAttribute(GlobalConstants.AUTHENTICATED_PROFILE);
			securityPrincipalProfile.setSecurityAccount(entity);
			this.httpSession.setAttribute(GlobalConstants.AUTHENTICATED_PROFILE, securityPrincipalProfile);

			System.out.println("Update account");
			Faces.redirect("/index.jsf");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void preProcessUserAccount() {
		if (null==this.entity.getId()){
			int latDotPos = this.entity.getEmail().lastIndexOf(CommonConstants.DOT_SIGN);
			String ssoId = this.entity.getEmail().substring(0, latDotPos)
					.replace(CommonConstants.DOT_SIGN, GlobeConstants.STRING_BLANK)
					.replace(CommonConstants.AT_SIGN, GlobeConstants.STRING_BLANK);
			this.entity.setUsername(ssoId);
		}
	}

	public void clear() {
		this.entity = UserPrincipal.builder().build();
		id = null;
	}

	public boolean isNew() {
		return this.entity == null || this.entity.getId() == null;
	}

	public UserPrincipal getEntity() {
		return entity;
	}

	public void setEntity(UserPrincipal entity) {
		this.entity = entity;
	}

	public void handleBusinessUnitSelect(SelectEvent<?> event) { 
		Object selectedObject = event.getObject(); 
		if (selectedObject instanceof BusinessUnit) {
			this.setBusinessUnit((BusinessUnit)selectedObject);
			this.entity.getContact().setBusinessUnit(this.businessUnit);
			//this.entity.setBusinessUnitCode(this.businessUnit.getCode());
		}
		//FacesMessage msg = new FacesMessage("Selected", "Item:" + item); 
	}

	public BusinessUnit getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

	protected boolean validate() {
		if (null==this.entity.getId() && this.businessService.existsByEmail(this.entity.getEmail()))
			return false;

		return true;
	}

	public void handleUpload(FileUploadEvent event) {
		this.entity.getContact().setProfilePicture(event.getFile().getContent());
	}

	public String getImageContentsAsBase64() {
    return Base64.getEncoder().encodeToString(this.entity.getContact().getProfilePicture());
	}
	
	@Override
	protected void onInit() {
		if (Faces.isAjaxRequest()) {
			return;
		}
		if (null != id) {
			this.entity = businessService.getObject(Long.valueOf(id));
		} else {
			this.entity = UserPrincipal.builder().build();
		}
	}

	@Override
	protected void onPreRenderComponent() {
		//this.globalDataRepository.dispatch(Context.builder().build().put(GlobeConstants.CONTEXT_DISPATCH_REPO_PATH, GlobeConstants.REPO_DIRECTORY + GlobeConstants.ALL_CSV_FILES));
		//this.globalEsiRepository.dispatch(GlobeConstants.REPO_DIRECTORY + GlobeConstants.ALL_CSV_FILES);
		System.out.println("DONE!!!!!");
	}

	public void processLandingData() {
		System.out.println("MasterLandingHome:processLandingData()");
	}
}