package net.ecology.auth.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ecology.auth.persistence.AccessPolicyPersistence;
import net.ecology.auth.persistence.GrantedAccessPolicyPersistence;
import net.ecology.auth.service.AccessPolicyService;
import net.ecology.common.CollectionsUtility;
import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.auth.GrantedAccessPolicy;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class AccessPolicyServiceImpl extends GenericService<AccessPolicy, Long> implements AccessPolicyService {
	private static final long serialVersionUID = 7987317340813933975L;

	@Inject 
	private AccessPolicyPersistence repository;

	@Inject
	private GrantedAccessPolicyPersistence grantedAccessPolicyRepository;
	
	protected IPersistence<AccessPolicy, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public List<AccessPolicy> getAccessPolicies(String accessPattern) {
		return this.repository.findByAccessPattern(accessPattern);
	}

	@Override
	public List<AccessPolicy> getAccessPoliciesByAuthority(Authority authority) {
		List<AccessPolicy> fetchedResults = CollectionsUtility.newList();
		List<GrantedAccessPolicy> accessDecisionAuthorities = grantedAccessPolicyRepository.findByAuthority(authority);
		for (GrantedAccessPolicy accessDecisionAuthority :accessDecisionAuthorities) {
			fetchedResults.add(accessDecisionAuthority.getAccessPolicy());
		}
		return fetchedResults;//this.repository.findByAuthority(authority);
	}
}
