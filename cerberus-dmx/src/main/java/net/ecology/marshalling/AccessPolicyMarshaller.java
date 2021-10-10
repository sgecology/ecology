/**
 * 
 */
package net.ecology.marshalling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.config.AccessPolicyMarshallConfig;
import net.ecology.entity.auth.AccessPolicy;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class AccessPolicyMarshaller extends MarshallerBasis<AccessPolicy, String[]> {
	@Override
	public AccessPolicy unmarshal(String[] elements) {
		return AccessPolicy.builder()
		    .accessPattern(elements[AccessPolicyMarshallConfig.accessPolicyAntMatcher.index()])
		    .build();		
	}
}