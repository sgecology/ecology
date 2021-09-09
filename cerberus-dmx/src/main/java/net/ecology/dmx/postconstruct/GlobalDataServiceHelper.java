/**
 * 
 *//*
package net.ecology.dmx.postconstruct;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.ecology.auth.comp.jwt.JsonWebTokenService;
import net.ecology.auth.domain.BaseACL;
import net.ecology.auth.service.AccessPolicyService;
import net.ecology.auth.service.AuthorityService;
import net.ecology.auth.service.UserAccountService;
import net.ecology.common.Base64Utils;
import net.ecology.common.CommonUtility;
import net.ecology.css.service.config.ConfigurationService;
import net.ecology.css.service.config.LanguageService;
import net.ecology.dmx.globe.DmxConstants;
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.config.Configuration;
import net.ecology.entity.general.Language;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.global.GlobalConstants;
import net.ecology.model.auth.AccessDecision;
import net.ecology.model.auth.UserAccountProfile;

*//**
 * @author ducbq
 *
 *//*
@Component
public class GlobalDataServiceHelper extends ComponentRoot {
	*//**
	 * 
	 *//*
	private static final long serialVersionUID = -3228342136333869857L;

	@Inject
  private Environment environment;

	@Inject
	private LanguageService languageService;

	//@Inject
	//private LingualHelper lingualHelper;

	@Inject
	private ConfigurationService configurationService;

	@Inject
	private AuthorityService authorityService;

	@Inject
	private AccessPolicyService accessDecisionPolicyService;

	@Inject 
	private PasswordEncoder passwordEncoder;

	@Inject
	private UserAccountService userAccountService;

	@Inject
	private JsonWebTokenService jwtServiceProvider;

	public void initialize() {
		initLanguages();
		initCountries();
	}

	public void initiateGlobalData() {
		initiateApplicatonProfile();
		setupMasterAuthorizations();
	}

	private void setupMasterAuthorizations() {
		setupMasterAuthorities();
		initAccessDecisionPolicies();
		setupMasterUsers();
	}

	private void initAccessDecisionPolicies() {
		String propAccessPattern = "accessPattern";
		Authority 
		administrator = authorityService.getByName(BaseACL.ADMINISTRATOR.getAuthority())
		, sysManager = authorityService.getByName(BaseACL.MANAGER.getAuthority())
		, regionalManager = authorityService.getByName(BaseACL.REGIONAL_MANAGER.getAuthority())
		, divisionManager = authorityService.getByName(BaseACL.DIVISION_MANAGER.getAuthority())
		, departmentManager = authorityService.getByName(BaseACL.DEPARTMENT_MANAGER.getAuthority())
		;

		if (!accessDecisionPolicyService.exists(propAccessPattern, BaseACL.ADMINISTRATOR.getAntMatcher())) {
			accessDecisionPolicyService.saveOrUpdate(AccessPolicy.builder().accessPattern(BaseACL.ADMINISTRATOR.getAntMatcher()).build().addAccessDecisionAuthority(administrator));
		}

		if (!accessDecisionPolicyService.exists(propAccessPattern, BaseACL.COORDINATOR.getAntMatcher())) {
			accessDecisionPolicyService.saveOrUpdate(AccessPolicy.builder().accessPattern(BaseACL.COORDINATOR.getAntMatcher()).build().addAccessDecisionAuthority(authorityService.getByName(BaseACL.COORDINATOR.getAuthority())));
		}

		if (!accessDecisionPolicyService.exists(propAccessPattern, BaseACL.CRSX.getAntMatcher())) {
			accessDecisionPolicyService.saveOrUpdate(AccessPolicy.builder().accessPattern(BaseACL.CRSX.getAntMatcher()).build().addAccessDecisionAuthority(authorityService.getByName(BaseACL.CRSX.getAuthority())));
		}

		if (!accessDecisionPolicyService.exists(propAccessPattern, BaseACL.MANAGER.getAntMatcher())) {
			accessDecisionPolicyService.saveOrUpdate(AccessPolicy.builder().accessPattern(BaseACL.MANAGER.getAntMatcher()).build().addAccessDecisionAuthority(sysManager));
		}

		if (!accessDecisionPolicyService.exists(propAccessPattern, BaseACL.OSX.getAntMatcher())){
			accessDecisionPolicyService.saveOrUpdate(
					AccessPolicy.builder()
					.accessPattern(BaseACL.OSX.getAntMatcher())
					.build().addAccessDecisionAuthority(authorityService.getByName(BaseACL.OSX.getAuthority()))
					);
		}
	
		if (!accessDecisionPolicyService.exists(propAccessPattern, BaseACL.SUBSCRIBER.getAntMatcher())) {
			accessDecisionPolicyService.saveOrUpdate(AccessDecisionPolicy.builder().accessPattern(BaseACL.SUBSCRIBER.getAntMatcher()).authority(authorityService.getByName(BaseACL.SUBSCRIBER.getAuthority())).build());
		}

		//One role can accesses to some access patterns
		String[] adminPatterns = new String[] {"/bszone/auxadmin/**", "/bszone/stock/**", "/admin/**", "/dbx/**", "/spaces/**", "/pages/**"};
		for (String adminPattern :adminPatterns) {
			if (!accessDecisionPolicyService.exists(propAccessPattern, adminPattern)) {
				accessDecisionPolicyService.saveOrUpdate(AccessPolicy.builder()
						.accessPattern(adminPattern)
						.accessDecision(AccessDecision.ACCESS_GRANTED)
						.build().addAccessDecisionAuthority(administrator));
			}
		}

		//One access pattern can be accessed by some roles
		String subscriptionAccessPatern = "/spaces/subscription/**";

		if (!accessDecisionPolicyService.exists(propAccessPattern, subscriptionAccessPatern)) {
			accessDecisionPolicyService.saveOrUpdate(AccessPolicy.builder()
					.accessPattern(subscriptionAccessPatern)
					.accessDecision(AccessDecision.ACCESS_GRANTED)
					.build()
					.addAccessDecisionAuthority(administrator)
					.addAccessDecisionAuthority(sysManager)
					.addAccessDecisionAuthority(regionalManager)
					.addAccessDecisionAuthority(divisionManager)
					.addAccessDecisionAuthority(departmentManager)
				);
		}
	}

	private void setupMasterAuthorities() {
		String propName = "name";
		//Setup master data for authorities
		if (!authorityService.exists(propName, BaseACL.SYS_ADMIN.getAuthority())) {
			authorityService.saveOrUpdate(Authority.builder()
					.name(BaseACL.SYS_ADMIN.getAuthority())
					.displayName(BaseACL.SYS_ADMIN.getAuthorityDisplayName())
					.build());
		}

		if (!authorityService.exists(propName, BaseACL.SYSTEM_ADMINISTRATOR.getAuthority())) {
			authorityService.saveOrUpdate(Authority.builder()
					.name(BaseACL.SYSTEM_ADMINISTRATOR.getAuthority())
					.displayName(BaseACL.SYSTEM_ADMINISTRATOR.getAuthorityDisplayName())
					.build());
		}

		if (!authorityService.exists(propName, BaseACL.ADMINISTRATOR.getAuthority())) {
			authorityService.saveOrUpdate(Authority.builder().name(BaseACL.ADMINISTRATOR.getAuthority()).displayName(BaseACL.ADMINISTRATOR.getAuthorityDisplayName()).build());
		}

		if (!authorityService.exists(propName, BaseACL.COORDINATOR.getAuthority())) {
			authorityService.saveOrUpdate(Authority.builder().name(BaseACL.COORDINATOR.getAuthority()).displayName(BaseACL.COORDINATOR.getAuthorityDisplayName()).build());
		}

		if (!authorityService.exists(propName, BaseACL.CRSX.getAuthority())){
			authorityService.saveOrUpdate(Authority.builder().name(BaseACL.CRSX.getAuthority()).displayName(BaseACL.CRSX.getAuthorityDisplayName()).build());
		}

		if (!authorityService.exists(propName, BaseACL.MANAGER.getAuthority())){
			authorityService.saveOrUpdate(Authority.builder().name(BaseACL.MANAGER.getAuthority()).displayName(BaseACL.MANAGER.getAuthorityDisplayName()).build());
		}

		if (!authorityService.exists(propName, BaseACL.OSX.getAuthority())){
			authorityService.saveOrUpdate(Authority.builder().name(BaseACL.OSX.getAuthority()).displayName(BaseACL.OSX.getAuthorityDisplayName()).build());
		}

		if (!authorityService.exists(propName, BaseACL.SUBSCRIBER.getAuthority())) {
			authorityService.saveOrUpdate(Authority.builder().name(BaseACL.SUBSCRIBER.getAuthority()).displayName(BaseACL.SUBSCRIBER.getAuthorityDisplayName()).build());
		}
		//Business privileges
		if (!authorityService.exists(propName, BaseACL.REGIONAL_MANAGER.getAuthority())) {
			authorityService.saveOrUpdate(Authority.builder().name(BaseACL.REGIONAL_MANAGER.getAuthority()).displayName(BaseACL.REGIONAL_MANAGER.getAuthorityDisplayName()).build());
		}

		if (!authorityService.exists(propName, BaseACL.DIVISION_MANAGER.getAuthority())) {
			authorityService.saveOrUpdate(Authority.builder().name(BaseACL.DIVISION_MANAGER.getAuthority()).displayName(BaseACL.DIVISION_MANAGER.getAuthorityDisplayName()).build());
		}

		if (!authorityService.exists(propName, BaseACL.DEPARTMENT_MANAGER.getAuthority())) {
			authorityService.saveOrUpdate(Authority.builder().name(BaseACL.DEPARTMENT_MANAGER.getAuthority()).displayName(BaseACL.DEPARTMENT_MANAGER.getAuthorityDisplayName()).build());
		}
	}

	private void setupMasterUsers() {
		String propSsoId = "ssoId";
		UserAccountProfile securityAccountProfile = null;
		if (!this.userAccountService.exists(propSsoId, BaseACL.SYS_ADMIN.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.SYS_ADMIN.getFirstName(), 
		  				BaseACL.SYS_ADMIN.getLastName(), 
		  				BaseACL.SYS_ADMIN.getUser(), 
		  				passwordEncoder.encode(BaseACL.SYS_ADMIN.getUser()), 
		  				BaseACL.SYS_ADMIN.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.SYS_ADMIN.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}

		if (!this.userAccountService.exists(propSsoId, BaseACL.SYSTEM_ADMINISTRATOR.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.SYSTEM_ADMINISTRATOR.getFirstName(), 
		  				BaseACL.SYSTEM_ADMINISTRATOR.getLastName(), 
		  				BaseACL.SYSTEM_ADMINISTRATOR.getUser(), 
		  				passwordEncoder.encode(BaseACL.SYSTEM_ADMINISTRATOR.getUser()), 
		  				BaseACL.SYSTEM_ADMINISTRATOR.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.SYSTEM_ADMINISTRATOR.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}

		if (!this.userAccountService.exists(propSsoId, BaseACL.ADMINISTRATOR.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.ADMINISTRATOR.getFirstName(), 
		  				BaseACL.ADMINISTRATOR.getLastName(), 
		  				BaseACL.ADMINISTRATOR.getUser(), 
		  				passwordEncoder.encode(BaseACL.ADMINISTRATOR.getUser()), 
		  				BaseACL.ADMINISTRATOR.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.ADMINISTRATOR.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}

		if (!this.userAccountService.exists(propSsoId, BaseACL.MANAGER.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.MANAGER.getFirstName(), 
		  				BaseACL.MANAGER.getLastName(), 
		  				BaseACL.MANAGER.getUser(), 
		  				passwordEncoder.encode(BaseACL.MANAGER.getUser()), 
		  				BaseACL.MANAGER.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.MANAGER.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}

		if (!this.userAccountService.exists(propSsoId, BaseACL.COORDINATOR.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.COORDINATOR.getFirstName(), 
		  				BaseACL.COORDINATOR.getLastName(), 
		  				BaseACL.COORDINATOR.getUser(), 
		  				passwordEncoder.encode(BaseACL.COORDINATOR.getUser()), 
		  				BaseACL.COORDINATOR.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.COORDINATOR.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}

		if (!this.userAccountService.exists(propSsoId, BaseACL.SUBSCRIBER.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.SUBSCRIBER.getFirstName(), 
		  				BaseACL.SUBSCRIBER.getLastName(), 
		  				BaseACL.SUBSCRIBER.getUser(), 
		  				passwordEncoder.encode(BaseACL.SUBSCRIBER.getUser()), 
		  				BaseACL.SUBSCRIBER.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}

		if (!this.userAccountService.exists(propSsoId, BaseACL.SUBSCRIBER_EXTERNAL.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.SUBSCRIBER_EXTERNAL.getFirstName(), 
		  				BaseACL.SUBSCRIBER_EXTERNAL.getLastName(), 
		  				BaseACL.SUBSCRIBER_EXTERNAL.getUser(), 
		  				passwordEncoder.encode(BaseACL.SUBSCRIBER_EXTERNAL.getUser()), 
		  				BaseACL.SUBSCRIBER_EXTERNAL.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER_EXTERNAL.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}

		if (!this.userAccountService.exists(propSsoId, BaseACL.SUBSCRIBER_INTERNAL.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.SUBSCRIBER_INTERNAL.getFirstName(), 
		  				BaseACL.SUBSCRIBER_INTERNAL.getLastName(), 
		  				BaseACL.SUBSCRIBER_INTERNAL.getUser(), 
		  				passwordEncoder.encode(BaseACL.SUBSCRIBER_INTERNAL.getUser()), 
		  				BaseACL.SUBSCRIBER_INTERNAL.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER_INTERNAL.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}

		if (!this.userAccountService.exists(propSsoId, BaseACL.CRSX.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.CRSX.getFirstName(), 
		  				BaseACL.CRSX.getLastName(), 
		  				BaseACL.CRSX.getUser(), 
		  				passwordEncoder.encode(BaseACL.CRSX.getUser()), 
		  				BaseACL.CRSX.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.CRSX.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}

		if (!this.userAccountService.exists(propSsoId, BaseACL.OSX.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.OSX.getFirstName(), 
		  				BaseACL.OSX.getLastName(), 
		  				BaseACL.OSX.getUser(), 
		  				passwordEncoder.encode(BaseACL.OSX.getUser()), 
		  				BaseACL.OSX.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.OSX.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}

		if (!this.userAccountService.exists(propSsoId, BaseACL.SUBSCRIBER_PROTECTED.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.SUBSCRIBER_PROTECTED.getFirstName(), 
		  				BaseACL.SUBSCRIBER_PROTECTED.getLastName(), 
		  				BaseACL.SUBSCRIBER_PROTECTED.getUser(), 
		  				passwordEncoder.encode(BaseACL.SUBSCRIBER_PROTECTED.getUser()), 
		  				BaseACL.SUBSCRIBER_PROTECTED.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER_PROTECTED.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}
		if (!this.userAccountService.exists(propSsoId, BaseACL.SUBSCRIBER_PRIVATE.getUser())) {
			securityAccountProfile = this.userAccountService.saveOrUpdate(
		  		UserAccountProfile.getInsance(
		  				BaseACL.SUBSCRIBER_PRIVATE.getFirstName(), 
		  				BaseACL.SUBSCRIBER_PRIVATE.getLastName(), 
		  				BaseACL.SUBSCRIBER_PRIVATE.getUser(), 
		  				passwordEncoder.encode(BaseACL.SUBSCRIBER_PRIVATE.getUser()), 
		  				BaseACL.SUBSCRIBER_PRIVATE.getEmail(), 
		  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER_PRIVATE.getAuthority())}));
			updateJWebToken(securityAccountProfile);
		}
	}

	private UserAccountProfile updateJWebToken(UserAccountProfile securityAccountProfile) {
		String indefiniteToken = this.jwtServiceProvider.generateIndefiniteToken(securityAccountProfile);
		indefiniteToken = Base64Utils.encode(indefiniteToken);
		securityAccountProfile.setToken(indefiniteToken);
		return this.userAccountService.saveOrUpdate(securityAccountProfile);
	}

	private void initiateApplicatonProfile() {
		final String defaultProductiveLink = "https://paramounts.herokuapp.com";
		final String defaultDevelopmentLink = "http://localhost:8080";
		
		String[] activeProfiles = null;
		String runningProfile = null;
		if (false==this.configurationService.isExistsByName(GlobalConstants.CONFIG_APP_ACCESS_URL)) {
			activeProfiles = environment.getActiveProfiles();
			if (CommonUtility.isNotEmpty(activeProfiles)) {
				runningProfile = activeProfiles[0];
			}

			this.configurationService.save(Configuration.builder()
					.group(GlobalConstants.CONFIG_GROUP_APP)
					.name(GlobalConstants.CONFIG_APP_ACCESS_URL)
					.value((runningProfile.contains("postgres") || runningProfile.contains("develop"))?defaultDevelopmentLink:defaultProductiveLink)
					.build());
		}
	}

	private void initLanguages() {
		logger.info("Enter languagues intialize");
		try {
			if (1 > this.languageService.count("code", DmxConstants.LANGUAGE_CODE_VIETNAM)) {
				this.languageService.saveOrUpdate(Language.builder().code(DmxConstants.LANGUAGE_CODE_VIETNAM).name("Vietnamese").build());
			}

			if (1 > this.languageService.count("code", DmxConstants.LANGUAGE_CODE_ENGLISH)) {
				this.languageService.saveOrUpdate(Language.builder().code(DmxConstants.LANGUAGE_CODE_ENGLISH).name("English").build());
			}
		} catch (Exception e) {
			logger.error(e);
		}
		logger.info("Leave languagues intialize");
	}

	private void initCountries() {
		logger.info("Enter countries intialize");
		//this.lingualHelper.initAvailableCountries();
		logger.info("Leave countries intialize");
	}

}
*/