/**
 * 
 */
package net.ecology.repository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.ecology.auth.certificate.TokenAuthenticationService;
import net.ecology.auth.certificate.ExpirationPolicy;
import net.ecology.auth.certificate.TokenGenerationPolicy;
import net.ecology.auth.certificate.TokenServiceHelper;
import net.ecology.auth.service.AccessPolicyService;
import net.ecology.auth.service.AuthorityService;
import net.ecology.auth.service.UserPrincipalService;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonConstants;
import net.ecology.common.CommonUtility;
import net.ecology.common.DateTimeUtility;
import net.ecology.config.AccessPolicyMarshallConfig;
import net.ecology.config.ContactMarshallConfig;
import net.ecology.css.service.config.ConfigurationService;
import net.ecology.css.service.contact.ContactService;
import net.ecology.dmx.manager.GlobalDmxManager;
import net.ecology.domain.AccessDecision;
import net.ecology.domain.Context;
import net.ecology.domain.MarshallingProvider;
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.auth.UserAccountProfile;
import net.ecology.entity.config.Configuration;
import net.ecology.entity.contact.Contact;
import net.ecology.entity.i18n.I18nLocale;
import net.ecology.entity.i18n.Message;
import net.ecology.entity.scheduler.Schedule;
import net.ecology.entity.scheduler.SchedulePlan;
import net.ecology.exceptions.CerberusException;
import net.ecology.factory.DataMarshallingFactory;
import net.ecology.framework.component.BasisComp;
import net.ecology.global.GlobalConstants;
import net.ecology.global.GlobeConstants;
import net.ecology.global.SchedulingConstants;
import net.ecology.helper.GlobalMarshalingHelper;
import net.ecology.instruments.base.IAdapter;
import net.ecology.instruments.base.Marshaller;
import net.ecology.lingual.service.LocaleService;
import net.ecology.lingual.service.MessageService;
import net.ecology.marshalling.AccessPolicyMarshaller;
import net.ecology.marshalling.ContactMarshallabler;
import net.ecology.marshalling.GlobalResourcesRepository;
import net.ecology.marshalling.MarshallingConstants;
import net.ecology.marshalling.MessageMarshaller;
import net.ecology.marshalling.ScheduleMarshaller;
import net.ecology.marshalling.SchedulePlanMarshaller;
import net.ecology.service.SchedulePlanService;
import net.ecology.service.ScheduleService;

/**
 * @author ducbq
 *
 */
@Component
@SuppressWarnings("unchecked")
public class DataInterchangeRepository extends BasisComp {
	private static final long serialVersionUID = -559448591301536785L;

  @Inject
  private TokenAuthenticationService jwtServiceProvider;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Inject
  private TokenServiceHelper tokenServiceHelper;

  @Inject
  private ContactService contactService;

  @Inject
  private UserPrincipalService userPrincipalService;

  @Inject
  private AuthorityService authorityService;

  @Inject
  private AccessPolicyService accessPolicyService;

	@Inject
  private Environment environment;

	@Inject
	protected ConfigurationService configurationService;

  @Inject
  private GlobalResourcesRepository globalResourcesRepository;

  @Inject
	private GlobalMarshalingHelper globalMarshalingHelper;

  @Inject
  private GlobalDmxManager globalDmxManager;

  @Inject
  private LocaleService localeService;

  @Inject
  private MessageService messageService;

  @Inject
  private ScheduleService scheduleService;

  @Inject
  private SchedulePlanService schedulePlanService;

  private static Marshaller<Contact, String[]> contactMarshallabler = ContactMarshallabler.builder().build();

  @Async
  public void dispatch(String dispatchRepoPath) throws CerberusException, IOException {
		logger.error("Enter DataInterchangeRepository.dispatch(String).");
		if (CommonUtility.isEmpty(dispatchRepoPath)) {
  		logger.error("The context or the default context attribute is empty!!!");
  		return;
  	}

		Context reservedStreams = null, dispatchedData = null;
		List<?> marshalledData = null;
		IAdapter csvAdapter = null, excelAdapter = null;
		if (!this.hasReserved()) {
			csvAdapter = DataMarshallingFactory.getAdapter(MarshallingProvider.PROVIDER_CSV);
			excelAdapter = DataMarshallingFactory.getAdapter(MarshallingProvider.PROVIDER_EXCEL);
			dispatchedData = Context.builder().build();

			reservedStreams = reserve(dispatchRepoPath);
			for (Object key :reservedStreams.keys()) {
				if (((String)key).endsWith(MarshallingProvider.PROVIDER_CSV.getExtension())){
					marshalledData = csvAdapter.marshall(reservedStreams.get(key), CommonConstants.PIPELINE, 0);
				} else if (((String)key).endsWith(MarshallingProvider.PROVIDER_EXCEL.getExtension())) {
					marshalledData = excelAdapter.marshall(reservedStreams.get(key), CommonConstants.PIPELINE, 0);
				}

				if (null != marshalledData) {
					dispatchedData.put((String)key, marshalledData);
				}
			}

			this.loadApplicationProfiles();
			this.loadAuthorization(dispatchedData);
			this.loadSchedules(dispatchedData);

			Collection<String[]> messages = (Collection<String[]>)dispatchedData.get("classpath:/repo/messages.csv");
			this.dispatchMessages(messages);

			this.globalMarshalingHelper.dispatch(dispatchedData);  	

			//Marks reserving data completed
			Configuration configuration = (Configuration)reservedStreams.get(GlobeConstants.REPOSITORY_MASTER_CONFIG);
  		this.configurationService.saveAndFlush(Configuration.builder()
  				.parent(configuration)
        	.group(GlobeConstants.CACHE_REPOSITORY)
        	.name(GlobeConstants.REPOSITORY_RESERVED)
        	.value(GlobeConstants.SETTLED)
        	.info("Configured data of: " + dispatchRepoPath)
        	.build()
        );
		}
		logger.error("DataInterchangeRepository.dispatch(String).");
	}

  private Map<String, String> builMasterConfigMap(List<String[]> masterConfigs) {
  	Map<String, String> masterConfigMap = CollectionsUtility.newHashedMap();
  	for (String[] masterConfigElements :masterConfigs) {
  		masterConfigMap.put(masterConfigElements[0], masterConfigElements[1]);
  	}
  	return masterConfigMap;
  }

  private void dispatchMessages(Collection<String[]> messages) {
		Collection<I18nLocale> i18nLocales = null;
		if (CommonUtility.isNotEmpty(messages)) {
			i18nLocales = dispatchLocales();
	  	dispatchMessages(messages, i18nLocales);
		}
  }

  private boolean hasReserved() {
  	Configuration configuration = this.configurationService.getByName(GlobeConstants.REPOSITORY_RESERVED).orElse(null);
  	if (null==configuration)
  		return false;

  	return GlobeConstants.SETTLED.equalsIgnoreCase(configuration.getValue());// this.configurationService.getByName(GlobeConstants.REPOSITORY_RESERVED).isPresent();  	
  }

  private Context reserve(String dispatchRepoPath) throws CerberusException, IOException{
  	Map<Object, Object> resourceMap = null;
  	Context result = null;
  	Configuration configuration = null;
  	Collection<String> archivedNames = null;
  	if (!this.configurationService.exists(GlobeConstants.PROP_NAME, GlobeConstants.CACHE_DISPATCH_REPOSITORY)) {
  		resourceMap = this.globalResourcesRepository.resources(dispatchRepoPath);
      if (CommonUtility.isEmpty(resourceMap)) {
      	logger.info("There is no data in path: " + dispatchRepoPath);
      	return null;
      }

    	archivedNames = this.globalDmxManager.archive(resourceMap, null);
  		configuration = Configuration.builder()
        	.group(GlobeConstants.CACHE_REPOSITORY)
        	.name(GlobeConstants.CACHE_DISPATCH_REPOSITORY)
        	.value(GlobeConstants.RAISED)
        	.valueExtended(String.join(GlobeConstants.PIPELINE, archivedNames))
        	.info("Cache data for access policy!")
        	.build();
  		this.configurationService.saveAndFlush(configuration);

  		for (String archivedName :archivedNames){
    		this.configurationService.saveAndFlush(Configuration.builder()
    				.parent(configuration)
          	.group(GlobeConstants.CACHE_REPOSITORY)
          	.name(archivedName)
          	.value(GlobeConstants.RAISED)
          	.valueExtended(archivedName)
          	.info("Configured data of: " + String.join(GlobeConstants.PIPELINE, archivedName))
          	.build()
          );
  		}
  	} 

  	//Unpack data
  	logger.info("There is data archived in database, loading from database. ");
  	configuration = this.configurationService.getByName(GlobeConstants.CACHE_DISPATCH_REPOSITORY).orElse(null);;
  	if (null != configuration) {
  		archivedNames = CollectionsUtility.newCollection();
  		for (Configuration config :configuration.getSubordinates()) {
  			archivedNames.add(config.getValueExtended());
  		}
  		result = this.globalDmxManager.unpack(archivedNames);
  	}

  	result.put(GlobeConstants.REPOSITORY_MASTER_CONFIG, configuration);
    return result;
  }

  private void loadAuthorization(Context context) {
    Collection<String[]> accessPolicies = null;
    for (Object key :context.keys()) {
    	if (((String)key).endsWith(MarshallingConstants.REPO_POLICY_DECISIONS)) {
    		accessPolicies = (Collection<String[]>)context.get(key);
    		break;
    	}
    }
    this.loadAccessPolicies(accessPolicies);

    //this.loadSecurityAccounts(accessPolicies);
  }

	private void loadSchedules(Context dataContext) throws CerberusException {
    if (CommonUtility.isEmpty(dataContext)) {
    	logger.info("An empty data context!!!");
    	return;
    }

		Configuration schedulesConfig = configurationService.getByName(SchedulingConstants.CONFIG_SCHEDULES).orElse(null);
		if (null!= schedulesConfig){
			logger.info("The application schedules is configured already!!");
			return;
		}

    Collection<String[]> schedules = null;
    for (Object key :dataContext.keys()) {
    	if (((String)key).endsWith(MarshallingConstants.REPO_SCHEDULES)) {
    		schedules = (Collection<String[]>)dataContext.get(key);
    		break;
    	}
    }

		if (CommonUtility.isEmpty(schedules)) {
			logger.info("There is no data of {} in data context", MarshallingConstants.REPO_SCHEDULES);
			return;
		}

		//Verify the first entry is column header:  "SP-Code", "SP-Name", "SP-StartTime", "SP-Job|SP-Type|Job-Crontime|Job-Class|Job-Code|Job-Name|Job-Display Name|Job-Category
		SchedulePlan schedulePlan = null;
		Schedule schedule = null;
		Marshaller<SchedulePlan, String[]> marshallerSP = SchedulePlanMarshaller.builder().build();
		Marshaller<Schedule, String[]> marshaller = ScheduleMarshaller.builder().build();
		for (String[] scheduleParts :schedules) {
			if (!this.schedulePlanService.exists(GlobeConstants.PROP_CODE, scheduleParts[SchedulingConstants.idx_SP_Code])) {
				schedulePlan = marshallerSP.unmarshal(scheduleParts);
				this.schedulePlanService.saveAndFlush(schedulePlan);
			}
			
			if (!this.scheduleService.exists(GlobeConstants.PROP_CODE, scheduleParts[SchedulingConstants.idx_JobCode])) {
				schedule = marshaller.unmarshal(scheduleParts);
				schedule.setSchedulePlan(schedulePlan);
				this.scheduleService.saveAndFlush(schedule);
			}
		}

		Configuration systemConfig = configurationService.getByName(SchedulingConstants.CONFIG_SYSTEM_CONFIG).orElse(null);
		if (null==systemConfig){
			systemConfig = Configuration.builder()
					.name(SchedulingConstants.CONFIG_SYSTEM_CONFIG)
					.value(SchedulingConstants.CONFIG_SYSTEM_CONFIG_VALUE)
					.build();

			configurationService.saveAndFlush(systemConfig);
		}
	}

	//-----------------------------------------------------------------I18n-Locale-----------------------------------------------------------------
	private Collection<I18nLocale> dispatchLocales() {
		final String languageProp = "language";
		Collection<I18nLocale> i18nLocales = CollectionsUtility.newCollection();
		logger.info("Enter dispatch locales");
		I18nLocale i18nLocale = null;
		if (!this.localeService.exists(languageProp, CommonUtility.LOCALE_USA.getLanguage())) {
			i18nLocale = I18nLocale.builder()
					.displayLanguage(CommonUtility.LOCALE_USA.getDisplayLanguage())
					.language(CommonUtility.LOCALE_USA.getLanguage())
					.build();
			this.localeService.saveAndFlush(i18nLocale);
			i18nLocales.add(i18nLocale);
		} else {
			i18nLocales.add(this.localeService.getLocale(CommonUtility.LOCALE_USA.getLanguage()));
		}
		
		if (!this.localeService.exists(languageProp, CommonUtility.LOCALE_VIETNAM.getLanguage())) {
			i18nLocale = I18nLocale.builder()
					.displayLanguage(CommonUtility.LOCALE_VIETNAM.getDisplayLanguage())
					.language(CommonUtility.LOCALE_VIETNAM.getLanguage())
					.build();
			this.localeService.saveAndFlush(i18nLocale);
			i18nLocales.add(i18nLocale);
		} else {
			i18nLocales.add(this.localeService.getLocale(CommonUtility.LOCALE_VIETNAM.getLanguage()));
		}
		logger.info("Leave dispatch locales");
		return i18nLocales;
	}

	private void dispatchMessages(Collection<String[]> messages, Collection<I18nLocale> i18nLocales) {
  	logger.info("Enter GlobalEsiRepository:dispatchMessages");
  	Marshaller<Message, String[]> messageMarshaller = MessageMarshaller.builder().build();
  	Message message = null;
  	Map<String, I18nLocale> localeMap = CollectionsUtility.newMap();
  	i18nLocales.forEach(action->{
  		localeMap.put(action.getLanguage(), action);
  	});

  	for (String[] messageParts :messages) {
  		try {
  			if (this.messageService.exists(localeMap.get(messageParts[MessageMarshaller.idx_locale]), messageParts[MessageMarshaller.idx_key]))
  				continue;

    		message = messageMarshaller.unmarshal(messageParts);
    		message.setLocale(localeMap.get(messageParts[MessageMarshaller.idx_locale]));
    		this.messageService.saveMessage(message);
			} catch (Exception e) {
				logger.error(e);
			}
  	}
  	logger.info("Leave GlobalEsiRepository:dispatchMessages");
	}
	//-----------------------------------------------------------------End of I18n-Locale----------------------------------------------------------

  private void loadApplicationProfiles() {
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

	private void loadAccessPolicies(Collection<String[]> accessPolicies){
    final String propAccessPattern = "accessPattern";

    if (CommonUtility.isEmpty(accessPolicies)) {
    	logger.info("There is no data for access policies");
    	return;
    }

    Marshaller<AccessPolicy, String[]> marshaller = AccessPolicyMarshaller.builder().build();
		Map<String, Authority> authorities = CollectionsUtility.newMap();
		Authority authority = null;
		AccessPolicy accessDecisionPolicy = null;
		for (String[] accessPolicyParts :accessPolicies){
			if (accessPolicyService.exists(propAccessPattern, accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyAuthority.index()]))
				continue;

			if (!authorities.containsKey(accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyAuthority.index()])){
				authority = authorityService.getByName(accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyAuthority.index()]);
			} else {
				authority = authorities.get(accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyAuthority.index()]);
			}

			if (null == authority){
				authority = Authority.builder()
						.name(accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyAuthority.index()])
						.displayName(accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyAuthorityDisplayName.index()])
						.build();
				authorityService.saveAndFlush(authority);
				authorities.put(authority.getName(), authority);
			}

			accessDecisionPolicy = marshaller.unmarshal(accessPolicyParts);
			if (null != accessDecisionPolicy) {
				accessDecisionPolicy.setAccessDecision(AccessDecision.ACCESS_GRANTED);
				accessDecisionPolicy.addAccessDecisionAuthority(authority);
				accessPolicyService.saveAndFlush(accessDecisionPolicy);
			}
			//accessDecisionPolicy = marshallAccessPolicy(authority, accessPolicyParts);
			
			this.loadUserAccountProfiles(accessPolicyParts, authority);
		}
	}

	/*private AccessPolicy marshallAccessPolicy(Authority authority, String[] parts){
		return AccessPolicy.builder()
    .accessPattern(parts[AccessPolicyMarshallConfig.accessPolicyAntMatcher.index()])
    .build()
    .addAccessDecisionAuthority(authority);		
	}*/

	/*private void loadSecurityAccounts(Collection<String[]> accessPolicies) {
    if (CommonUtility.isEmpty(accessPolicies)) {
    	logger.info("There is no data for security account");
    	return;
    }

		for (String[] accessPolicyParts :accessPolicies){
			this.loadUserPrincipal(accessPolicyParts);
		}
	}	*/

  private Contact loadContact(String[] accessPolicyParts) {
  	Contact loadedContact = null;
  	if (!this.contactService.exists(GlobeConstants.PROP_CODE, accessPolicyParts[AccessPolicyMarshallConfig.accessPolicySsoId.index()])) {
    	String[] contactParts = buildContactParts(accessPolicyParts);
    	loadedContact = contactMarshallabler.unmarshal(contactParts);
    	this.contactService.saveAndFlush(loadedContact);
  	} else {
  		loadedContact = this.contactService.getObjectByCode(accessPolicyParts[AccessPolicyMarshallConfig.accessPolicySsoId.index()]);
  	}
  	return loadedContact;
  }

  private UserAccountProfile loadUserAccountProfiles(String[] accessPolicyParts, Authority authority) {
		final String propSsoId = "username";
  	UserAccountProfile userAccountProfile = null;
  	String token = null;
  	if (!this.userPrincipalService.exists(propSsoId, accessPolicyParts[AccessPolicyMarshallConfig.accessPolicySsoId.index()])) {
  		userAccountProfile = UserAccountProfile.valueOf(
  				accessPolicyParts[AccessPolicyMarshallConfig.accessPolicySsoId.index()], 
					this.passwordEncoder.encode(accessPolicyParts[AccessPolicyMarshallConfig.accessPolicySsoId.index()]), 
					accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyEmail.index()], 
					new Authority[] {authority}, 
					accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyFirstName.index()], 
					accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyLastName.index()]);

  		token = this.tokenServiceHelper.generateToken(userAccountProfile, ExpirationPolicy.tokenExpirationFourWeeks, TokenGenerationPolicy.SsoIdEmail);
  		userAccountProfile.setToken(token);
  		userAccountProfile.setRegisteredDate(DateTimeUtility.systemDateTime());
  		userAccountProfile.setEnabledDate(userAccountProfile.getRegisteredDate());
  		userAccountProfile.setVisible(Boolean.TRUE);
  		userAccountProfile.setEnabled(Boolean.TRUE);
  		userAccountProfile.setExpirationPolicy((short)ExpirationPolicy.expirationInMonth.getExpiredPolicy());
  		this.userPrincipalService.save(userAccountProfile);
		}
  	return userAccountProfile;
  }

  private String[] buildContactParts(String[] accessPolicyParts) {
  	String[] contactParts = new String[ContactMarshallConfig.values().length];
  	contactParts[ContactMarshallConfig.idxCode.index()] = accessPolicyParts[AccessPolicyMarshallConfig.accessPolicySsoId.index()];
  	contactParts[ContactMarshallConfig.idxFirstName.index()] = accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyFirstName.index()];
  	contactParts[ContactMarshallConfig.idxLastName.index()] = accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyLastName.index()];
  	contactParts[ContactMarshallConfig.idxEmail.index()] = accessPolicyParts[AccessPolicyMarshallConfig.accessPolicyEmail.index()];

  	return contactParts;
  }

  /*private UserPrincipal loadUserPrincipal(BaseACL baseACL) {
		final String propSsoId = "username";
  	UserPrincipal userPrincipal = null;
  	Contact contact = null;
  	if (!this.userPrincipalService.exists(propSsoId, baseACL.getUser())) {
  		contact = buildContact(null);
    	this.contactService.saveAndFlush(contact);

  		userPrincipal = UserPrincipal.valueOf(baseACL.getUser(), 
					this.passwordEncoder.encode(baseACL.getUser()), 
					baseACL.getEmail(), 
					new Authority[] {authorityService.getByName(baseACL.getAuthority())});

  		userPrincipal.setContact(contact);
  		this.userPrincipalService.save(userPrincipal);
			syncSecurityToken(userPrincipal);
		}
  	return userPrincipal;
  }*/

	private UserAccountProfile syncSecurityToken(UserAccountProfile authProfile) {
		String indefiniteToken = this.jwtServiceProvider.generateIndefiniteToken(authProfile);
		authProfile.setToken(indefiniteToken);
		this.userPrincipalService.save((UserAccountProfile) authProfile);
		return (UserAccountProfile) authProfile;
	}
}