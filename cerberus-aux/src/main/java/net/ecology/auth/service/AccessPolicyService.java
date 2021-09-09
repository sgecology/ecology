package net.ecology.auth.service;

import java.util.List;

import net.ecology.entity.auth.AccessPolicy;
import net.ecology.entity.auth.Authority;
import net.ecology.framework.service.IGenericService;

public interface AccessPolicyService extends IGenericService<AccessPolicy, Long> {
	List<AccessPolicy> getAccessPolicies(String accessPattern);
	List<AccessPolicy> getAccessPoliciesByAuthority(Authority authority);
}
