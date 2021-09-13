/**
 * 
 */
package net.ecology.auth;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.ecology.auth.comp.jwt.JsonWebTokenService;
import net.ecology.auth.domain.BaseACL;
import net.ecology.auth.service.AccessPolicyService;
import net.ecology.auth.service.AuthorityService;
import net.ecology.auth.service.UserPrincipalService;
import net.ecology.base.DataAdapter;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonConstants;
import net.ecology.common.CommonUtility;
import net.ecology.css.service.config.ConfigurationService;
import net.ecology.css.service.contact.ContactService;
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.entity.auth.base.PrincipalDetails;
import net.ecology.entity.config.Configuration;
import net.ecology.entity.contact.Contact;
import net.ecology.entity.general.Catalogue;
import net.ecology.factory.DataMarshallingFactory;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.global.GlobalConstants;
import net.ecology.global.GlobeConstants;
import net.ecology.globe.GlobalMarshallingRepository;
import net.ecology.globe.MarshallingConstants;
import net.ecology.lingual.service.LocaleService;
import net.ecology.model.Context;
import net.ecology.model.IOContainer;
import net.ecology.model.MarshallingProvider;
import net.ecology.model.XWorksheet;
import net.ecology.model.auth.AccessDecision;
import net.ecology.service.general.CatalogueService;

/**
 * @author ducbq
 *
 */
@Component
public class GlobalMarshalingHelper extends ComponentRoot {
	static final int IDX_antMatcher = 0;
	static final int IDX_authority = 1;
	static final int IDX_authorityDisplayName = 2;
	static final int IDX_userName = 3;
	static final int IDX_email = 4;
	static final int IDX_firstName = 5;
	static final int IDX_lastName = 6;

	private static final long serialVersionUID = 6305779930492008393L;

	@Inject
  private Environment environment;

  @Inject
  private ConfigurationService configurationService;

  @Inject
  private AuthorityService authorityService;

  @Inject
  private AccessPolicyService accessPolicyService;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Inject
  private UserPrincipalService securityAccountService;

  @Inject
  private JsonWebTokenService jwtServiceProvider;

  @Inject
  private CatalogueService catalogueService;

  @Inject
  private GlobalMarshallingRepository globalMarshallingRepository;

  @Inject
  private ContactService contactService;
  
  @Inject
  private LocaleService localeService;

  private void setupMasterAuthorizations() {
    this.loadMasterAuthorities();
    this.loadMasterSecurityOptions();
    this.loadAccessPolicies();
  }

  private void loadAccessPolicies() {
    String propAccessPattern = "accessPattern";
		Authority administrator = authorityService.getByName(BaseACL.ADMINISTRATOR.getAuthority())
			, sysManager = authorityService.getByName(BaseACL.MANAGER.getAuthority())
			, regionalManager = authorityService.getByName(BaseACL.REGIONAL_MANAGER.getAuthority())
			, divisionManager = authorityService.getByName(BaseACL.DIVISION_MANAGER.getAuthority())
			, departmentManager = authorityService.getByName(BaseACL.DEPARTMENT_MANAGER.getAuthority())
			;

    if (!accessPolicyService.exists(propAccessPattern, BaseACL.ADMINISTRATOR.getAntMatcher())) {
      accessPolicyService.saveOrUpdate(
          AccessPolicy.builder()
          .accessPattern(BaseACL.ADMINISTRATOR.getAntMatcher())
          .build()
          .addAccessDecisionAuthority(administrator)
       );
    }

    if (!accessPolicyService.exists(propAccessPattern, BaseACL.COORDINATOR.getAntMatcher())) {
      accessPolicyService.saveOrUpdate(AccessPolicy.builder().accessPattern(BaseACL.COORDINATOR.getAntMatcher()).build()
          .addAccessDecisionAuthority(authorityService.getByName(BaseACL.COORDINATOR.getAuthority())));
    }

    if (!accessPolicyService.exists(propAccessPattern, BaseACL.CRSX.getAntMatcher())) {
      accessPolicyService.saveOrUpdate(AccessPolicy.builder().accessPattern(BaseACL.CRSX.getAntMatcher()).build()
          .addAccessDecisionAuthority(authorityService.getByName(BaseACL.CRSX.getAuthority())));
    }

    if (!accessPolicyService.exists(propAccessPattern, BaseACL.MANAGER.getAntMatcher())) {
      accessPolicyService
          .saveOrUpdate(AccessPolicy.builder().accessPattern(BaseACL.MANAGER.getAntMatcher()).build().addAccessDecisionAuthority(sysManager));
    }

			if (!accessPolicyService.exists(propAccessPattern, BaseACL.OSX.getAntMatcher())){
				accessPolicyService.saveOrUpdate(
						AccessPolicy.builder()
						.accessPattern(BaseACL.OSX.getAntMatcher())
						.build().addAccessDecisionAuthority(authorityService.getByName(BaseACL.OSX.getAuthority()))
						);
			}
		
			/*if (!accessDecisionPolicyService.exists(propAccessPattern, BaseACL.SUBSCRIBER.getAntMatcher())) {
				accessDecisionPolicyService.saveOrUpdate(AccessDecisionPolicy.builder().accessPattern(BaseACL.SUBSCRIBER.getAntMatcher()).authority(authorityService.getByName(BaseACL.SUBSCRIBER.getAuthority())).build());
			}*/

			//One role can accesses to some access patterns
			String[] adminPatterns = new String[] {"/bszone/auxadmin/**", "/bszone/stock/**", "/admin/**", "/dbx/**", "/spaces/**", "/pages/**", "/user/**", "/pages/public/**"};
			for (String adminPattern :adminPatterns) {
				if (!accessPolicyService.exists(propAccessPattern, adminPattern)) {
					accessPolicyService.saveOrUpdate(AccessPolicy.builder()
							.accessPattern(adminPattern)
							.accessDecision(AccessDecision.ACCESS_GRANTED)
							.build().addAccessDecisionAuthority(administrator));
				}
			}

			//One access pattern can be accessed by some roles
			String subscriptionAccessPatern = "/spaces/subscription/**";

			if (!accessPolicyService.exists(propAccessPattern, subscriptionAccessPatern)) {
				accessPolicyService.saveOrUpdate(AccessPolicy.builder()
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

		private void loadMasterAuthorities() {
			String propName = GlobeConstants.PROP_NAME;
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

		private void loadMasterSecurityAccounts(Context context) {
			for (BaseACL baseACL :BaseACL.values()) {
				try {
					loadUserPrincipal(baseACL);
				} catch (Exception e) {
					loadUserPrincipal(baseACL);
					e.printStackTrace();
				}
			}
			
			
			
			
			
			/*if (!this.securityAccountService.exists(propSsoId, BaseACL.SYS_ADMIN.getUser())) {
				securityAccount = this.securityAccountService.save(
			  		UserPrincipal.getInsance(
			  				BaseACL.SYS_ADMIN.getFirstName(), 
			  				BaseACL.SYS_ADMIN.getLastName(), 
			  				BaseACL.SYS_ADMIN.getUser(), 
			  				passwordEncoder.encode(BaseACL.SYS_ADMIN.getUser()), 
			  				BaseACL.SYS_ADMIN.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.SYS_ADMIN.getAuthority())}
			  				));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.SYSTEM_ADMINISTRATOR.getUser())) {
				securityAccount = this.securityAccountService.save(
			  		UserPrincipal.getInsance(
			  				BaseACL.SYSTEM_ADMINISTRATOR.getFirstName(), 
			  				BaseACL.SYSTEM_ADMINISTRATOR.getLastName(), 
			  				BaseACL.SYSTEM_ADMINISTRATOR.getUser(), 
			  				passwordEncoder.encode(BaseACL.SYSTEM_ADMINISTRATOR.getUser()), 
			  				BaseACL.SYSTEM_ADMINISTRATOR.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.SYSTEM_ADMINISTRATOR.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.ADMINISTRATOR.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.ADMINISTRATOR.getFirstName(), 
			  				BaseACL.ADMINISTRATOR.getLastName(), 
			  				BaseACL.ADMINISTRATOR.getUser(), 
			  				passwordEncoder.encode(BaseACL.ADMINISTRATOR.getUser()), 
			  				BaseACL.ADMINISTRATOR.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.ADMINISTRATOR.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.MANAGER.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.MANAGER.getFirstName(), 
			  				BaseACL.MANAGER.getLastName(), 
			  				BaseACL.MANAGER.getUser(), 
			  				passwordEncoder.encode(BaseACL.MANAGER.getUser()), 
			  				BaseACL.MANAGER.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.MANAGER.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.COORDINATOR.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.COORDINATOR.getFirstName(), 
			  				BaseACL.COORDINATOR.getLastName(), 
			  				BaseACL.COORDINATOR.getUser(), 
			  				passwordEncoder.encode(BaseACL.COORDINATOR.getUser()), 
			  				BaseACL.COORDINATOR.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.COORDINATOR.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.SUBSCRIBER.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.SUBSCRIBER.getFirstName(), 
			  				BaseACL.SUBSCRIBER.getLastName(), 
			  				BaseACL.SUBSCRIBER.getUser(), 
			  				passwordEncoder.encode(BaseACL.SUBSCRIBER.getUser()), 
			  				BaseACL.SUBSCRIBER.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.SUBSCRIBER_EXTERNAL.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.SUBSCRIBER_EXTERNAL.getFirstName(), 
			  				BaseACL.SUBSCRIBER_EXTERNAL.getLastName(), 
			  				BaseACL.SUBSCRIBER_EXTERNAL.getUser(), 
			  				passwordEncoder.encode(BaseACL.SUBSCRIBER_EXTERNAL.getUser()), 
			  				BaseACL.SUBSCRIBER_EXTERNAL.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER_EXTERNAL.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.SUBSCRIBER_INTERNAL.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.SUBSCRIBER_INTERNAL.getFirstName(), 
			  				BaseACL.SUBSCRIBER_INTERNAL.getLastName(), 
			  				BaseACL.SUBSCRIBER_INTERNAL.getUser(), 
			  				passwordEncoder.encode(BaseACL.SUBSCRIBER_INTERNAL.getUser()), 
			  				BaseACL.SUBSCRIBER_INTERNAL.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER_INTERNAL.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.CRSX.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.CRSX.getFirstName(), 
			  				BaseACL.CRSX.getLastName(), 
			  				BaseACL.CRSX.getUser(), 
			  				passwordEncoder.encode(BaseACL.CRSX.getUser()), 
			  				BaseACL.CRSX.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.CRSX.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.OSX.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.OSX.getFirstName(), 
			  				BaseACL.OSX.getLastName(), 
			  				BaseACL.OSX.getUser(), 
			  				passwordEncoder.encode(BaseACL.OSX.getUser()), 
			  				BaseACL.OSX.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.OSX.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.SUBSCRIBER_PROTECTED.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.SUBSCRIBER_PROTECTED.getFirstName(), 
			  				BaseACL.SUBSCRIBER_PROTECTED.getLastName(), 
			  				BaseACL.SUBSCRIBER_PROTECTED.getUser(), 
			  				passwordEncoder.encode(BaseACL.SUBSCRIBER_PROTECTED.getUser()), 
			  				BaseACL.SUBSCRIBER_PROTECTED.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER_PROTECTED.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.SUBSCRIBER_PRIVATE.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.SUBSCRIBER_PRIVATE.getFirstName(), 
			  				BaseACL.SUBSCRIBER_PRIVATE.getLastName(), 
			  				BaseACL.SUBSCRIBER_PRIVATE.getUser(), 
			  				passwordEncoder.encode(BaseACL.SUBSCRIBER_PRIVATE.getUser()), 
			  				BaseACL.SUBSCRIBER_PRIVATE.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER_PRIVATE.getAuthority())}));
				syncSecurityToken(securityAccount);
			}

			if (!this.securityAccountService.exists(propSsoId, BaseACL.SUBSCRIBER_RESTRICTED.getUser())) {
				securityAccount = this.securityAccountService.save(
						UserPrincipal.getInsance(
			  				BaseACL.SUBSCRIBER_RESTRICTED.getFirstName(), 
			  				BaseACL.SUBSCRIBER_RESTRICTED.getLastName(), 
			  				BaseACL.SUBSCRIBER_RESTRICTED.getUser(), 
			  				passwordEncoder.encode(BaseACL.SUBSCRIBER_RESTRICTED.getUser()), 
			  				BaseACL.SUBSCRIBER_RESTRICTED.getEmail(), 
			  				new Authority[] {authorityService.getByName(BaseACL.SUBSCRIBER_RESTRICTED.getAuthority())}));
				syncSecurityToken(securityAccount);
			}*/
		}

	private UserPrincipal syncSecurityToken(PrincipalDetails authProfile) {
		String indefiniteToken = this.jwtServiceProvider.generateIndefiniteToken(authProfile);
		authProfile.setToken(indefiniteToken);
		this.securityAccountService.save((UserPrincipal) authProfile);
		return (UserPrincipal) authProfile;
	}

	private Context loadAuthorizationData(Context context){
		IOContainer ioContainer = this.globalMarshallingRepository.resourceAsIOContainer(MarshallingConstants.REPO_POLICY_DECISIONS);
		if (CommonUtility.isEmpty(ioContainer) || ioContainer.isEmpty())
			return null;

		DataAdapter dataMarshaller = DataMarshallingFactory.getAdapter(MarshallingProvider.DATA_PROVIDER_CSV);

		context.put(CommonConstants.IO_DATA_CONTAINER, ioContainer);
		if (context.containsKey(CommonConstants.SEPARATOR)){
			context.put(CommonConstants.SEPARATOR, CommonConstants.PIPELINE);
		}

		if (context.containsKey(CommonConstants.SKIP_LINES)){
			context.put(CommonConstants.SKIP_LINES, 0);
		}

		Context restContext = dataMarshaller.marshal(context);
		if (CommonUtility.isEmpty(restContext) || restContext.isEmpty())
			return null;

		return restContext;
	}

	@SuppressWarnings("unchecked")
	private Context loadMasterAccessPolicies(Context context){
    String propAccessPattern = "accessPattern";

    if (CommonUtility.isEmpty(context))
    	return null;

    List<String[]> fetchedData = (List<String[]>)context.get(Context.DEFAULT);
		if (CommonUtility.isEmpty(fetchedData))
			return null;

		/*
		antMatcher|authority|authorityDisplayName|userName|email|firstName|lastName
		*/
		Map<String, Authority> authorities = CollectionsUtility.newMap();
		Authority authority = null;
		AccessPolicy accessDecisionPolicy = null;
		for (String[] values :fetchedData){
			if (accessPolicyService.exists(propAccessPattern, values[IDX_authority]))
				continue;

			if (!authorities.containsKey(values[IDX_authority])){
				authority = authorityService.getByName(values[IDX_authority]);
			} else {
				authority = authorities.get(values[IDX_authority]);
			}

			if (null == authority){
				authority = Authority.builder()
						.name(values[IDX_authority])
						.displayName(values[IDX_authorityDisplayName])
						.build();
				authorityService.saveAndFlush(authority);
				authorities.put(authority.getName(), authority);
			}

			accessDecisionPolicy = marshallAccessDecisionPolicy(authority, values);
			accessPolicyService.saveAndFlush(accessDecisionPolicy);
		}

		return null;
	}

	private AccessPolicy marshallAccessDecisionPolicy(Authority authority, String[] parts){
		return AccessPolicy.builder()
    .accessPattern(parts[IDX_antMatcher])
    .build()
    .addAccessDecisionAuthority(authority);		
	}

	private void loadMasterSecurityOptions() {
		Context context = Context.builder().build()
				.put(CommonConstants.SEPARATOR, CommonConstants.PIPELINE)
				.put(CommonConstants.SKIP_LINES, 0);

		context = loadAuthorizationData(context);
		
    loadMasterAccessPolicies(context);

    //setupMasterAccessDecisionPolicies();

    loadMasterSecurityAccounts(context);

	}

  private void setupMasterApplicationProfile() {
    final String defaultProductiveLink = "https://paramounts.herokuapp.com";
    final String defaultDevelopmentLink = "http://localhost:8181";

    String[] activeProfiles = null;
    String runningProfile = null;
    if (false == this.configurationService.isExistsByName(GlobalConstants.CONFIG_APP_ACCESS_URL)) {
      activeProfiles = environment.getActiveProfiles();
      if (CommonUtility.isNotEmpty(activeProfiles)) {
        runningProfile = activeProfiles[0];
      }

      this.configurationService.save(Configuration.builder()
          .group(GlobalConstants.CONFIG_GROUP_APP)
          .name(GlobalConstants.CONFIG_APP_ACCESS_URL)
          .value((runningProfile.contains("postgres") || runningProfile.contains("develop")) ? defaultDevelopmentLink : defaultProductiveLink)
          .build());
    }
  }

  public void dispatch(Context context) {
    logger.info("Enter GlobalMarshalingHelper.dispatch()");
    try {
    	setupMasterApplicationProfile();
      setupMasterAuthorizations();
      marshallingCatalogues(context);
		} catch (Exception e) {
			logger.error(e);
		}
    logger.info("Leave GlobalMarshalingHelper.dispatch()");
  }

	private void marshallingCatalogues(Context context){
  	XWorksheet worksheet = (XWorksheet)context.get(MarshallingConstants.CATALOGUE_DATA_FILE);
		if (null==worksheet)
			return;

		List<?> catalogueParts = null;
  	Catalogue catalogue = null;
  	for (Object key :worksheet.keys()){
  		catalogueParts = worksheet.get(key);
  		if (!this.catalogueService.exists(GlobeConstants.PROP_CODE, catalogueParts.get(1))){
    		catalogue = marshallCatalogue(catalogueParts);
    		this.catalogueService.save(catalogue);
  		}
  	}
  }

  private Catalogue marshallCatalogue(List<?> parts){
  	Catalogue marshalledObj = Catalogue.builder()
  			.subtype((String)parts.get(0))
  			.code((String)parts.get(1))
  			.name((String)parts.get(2))
  			.build();

  	return marshalledObj;
  }

  private Contact buildContact(BaseACL baseACL) {
  	return Contact.builder()
  	.code(baseACL.getUser())
  	.firstName(baseACL.getFirstName())
  	.lastName(baseACL.getLastName())
  	.build();
  }

  private UserPrincipal loadUserPrincipal(BaseACL baseACL) {
		final String propSsoId = "username";
  	UserPrincipal userPrincipal = null;
  	Contact contact = null;
  	if (!this.securityAccountService.exists(propSsoId, baseACL.getUser())) {
  		contact = buildContact(baseACL);
    	this.contactService.saveAndFlush(contact);

  		userPrincipal = UserPrincipal.valueOf(baseACL.getUser(), 
					passwordEncoder.encode(baseACL.getUser()), 
					baseACL.getEmail(), 
					new Authority[] {authorityService.getByName(baseACL.getAuthority())});

  		userPrincipal.setContact(contact);
  		this.securityAccountService.save(userPrincipal);
			syncSecurityToken(userPrincipal);
		}
  	return userPrincipal;
  }
}