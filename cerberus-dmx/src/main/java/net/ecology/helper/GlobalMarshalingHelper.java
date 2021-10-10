/**
 * 
 */
package net.ecology.helper;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.common.CommonUtility;
import net.ecology.domain.Context;
import net.ecology.entity.general.Catalogue;
import net.ecology.framework.component.BasisComp;
import net.ecology.global.GlobeConstants;
import net.ecology.instruments.base.Marshaller;
import net.ecology.marshalling.CatalogueMarshaller;
import net.ecology.marshalling.MarshallingConstants;
import net.ecology.service.general.CatalogueService;

/**
 * @author ducbq
 *
 */
@Component
public class GlobalMarshalingHelper extends BasisComp {
	private static final long serialVersionUID = 6305779930492008393L;

  @Inject
  private CatalogueService catalogueService;

 /* private void loadAccessPolicies() {
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
		
			if (!accessDecisionPolicyService.exists(propAccessPattern, BaseACL.SUBSCRIBER.getAntMatcher())) {
				accessDecisionPolicyService.saveOrUpdate(AccessDecisionPolicy.builder().accessPattern(BaseACL.SUBSCRIBER.getAntMatcher()).authority(authorityService.getByName(BaseACL.SUBSCRIBER.getAuthority())).build());
			}

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
		}*/

		/*private void loadMasterAuthorities() {
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
		}*/

  public void dispatch(Context dataContext) {
    logger.info("Enter GlobalMarshalingHelper.dispatch()");
    try {
      marshallingCatalogues(dataContext);
		} catch (Exception e) {
			logger.error(e);
		}
    logger.info("Leave GlobalMarshalingHelper.dispatch()");
  }

	private void marshallingCatalogues(Context context){
		String repoDir = (String)context.get(GlobeConstants.PROP_REPO_DIR);
  	Collection<String[]> catalogues = (Collection<String[]>)context.get(repoDir + MarshallingConstants.REPO_CATALOGUES);
		if (CommonUtility.isEmpty(catalogues) || catalogues.isEmpty()) {
			logger.info("There is no data of {} in data context", MarshallingConstants.REPO_CATALOGUES);
			return;
		}

		Marshaller<Catalogue, String[]> marshaller = CatalogueMarshaller.builder().build();
  	Catalogue catalogue = null;
  	for (String[] catalogueParts :catalogues){
  		if (!this.catalogueService.exists(GlobeConstants.PROP_CODE, catalogueParts[CatalogueMarshaller.idx_code])){
    		catalogue = marshaller.unmarshal(catalogueParts);
    		this.catalogueService.save(catalogue);
  		}
  	}
  }

}