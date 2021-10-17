/**
 * 
 */
package net.ecology.entity.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;
import net.ecology.common.CollectionsUtility;

/**
 * @author ducbq
 *
 */
public class TokenizeAuthentication extends UsernamePasswordAuthenticationToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8667459050189011275L;

	@Setter @Getter
	private List<AccessPolicy> accessPolicies = CollectionsUtility.newList();

	public TokenizeAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public TokenizeAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, List<AccessPolicy> accessPolicies) {
		super(principal, credentials, authorities);
		this.accessPolicies.addAll(accessPolicies); 
	}
}