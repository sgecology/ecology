/**
 * 
 */
package net.ecology.model.auth;

import java.util.Iterator;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ecology.common.CollectionsUtility;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.framework.entity.Repo;
import net.ecology.global.GlobeConstants;

/**
 * @author ducbq
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountProfile extends Repo {
	private static final long serialVersionUID = -1051763928685608384L;

	@Setter @Getter
	private UserPrincipal securityAccount;

	private Authentication authentication;

	@Builder.Default
	private List<Authority> grantedAuthorities = CollectionsUtility.newList();

	@Builder.Default
	private String displayName = GlobeConstants.ANONYMOUS_USER;

	public String getDisplayName() {
		if (null != this.securityAccount) {
			displayName = this.securityAccount.getDisplayName();
		}

		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isPresentUserAccount() {
		return (null != this.securityAccount);
	}

	public List<Authority> getGrantedAuthorities() {
		return grantedAuthorities;
	}

	public void setGrantedAuthorities(List<Authority> grantedAuthorities) {
		this.grantedAuthorities.addAll(grantedAuthorities);
	}

	public void addGrantedAuthority(Authority grantedAuthority) {
		this.grantedAuthorities.add(grantedAuthority);
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	public String getDisplayRoles() {
		StringBuilder displayRoles = new StringBuilder();
		Iterator<? extends GrantedAuthority> itr = this.getGrantedAuthorities().iterator();
		Authority currentAuthority = null;
		while (itr.hasNext()) {
			currentAuthority = (Authority)itr.next();
			displayRoles.append(currentAuthority.getName());
			if (itr.hasNext()) {
				displayRoles.append(GlobeConstants.SEPARATOR_SEMICOLON);
			}
		}
		return displayRoles.toString();
	}
}
