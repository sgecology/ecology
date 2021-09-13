/*
\* Copyright 2017, Bui Quy Duc
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package net.ecology.entity.auth;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.DateTimeUtility;
import net.ecology.entity.auth.base.PrincipalDetails;
import net.ecology.entity.auth.base.PrincipalEntity;
import net.ecology.entity.contact.Contact;
import net.ecology.model.DateTimePatterns;

/**
 * A user. UserAccountProfile
 * 
 * @author bqduc
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "aux_security_account")
@ToString(exclude = { "grantedAuthorities" })
@EqualsAndHashCode(callSuper = true)
public class UserPrincipal extends PrincipalEntity implements PrincipalDetails {
	private static final long serialVersionUID = 6867374706357215118L;

	@Setter @Getter
	@Transient
	private UserDetails userDetails;

	@Setter @Getter
	@Builder.Default
	@LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy="principal", cascade=CascadeType.ALL)
  private List<GrantedPermission> grantedAuthorities = CollectionsUtility.newList();
	
	@Setter @Getter
	@Column(name = "info", columnDefinition="text")
	private String info;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "contact_id")
	private Contact contact;	
	
	@Transient
	public String getDisplayName() {
		return this.contact.getDisplayName();
	}

	@Transient
	public String getDisplayIssueDate() {
		return DateTimeUtility.dateToString(this.getEnabledDate(), DateTimePatterns.ddMMyyyy_SLASH.getDateTimePattern());
	}

	public UserPrincipal addPrivilege(GrantedPermission userAccountPrivilege) {
		this.grantedAuthorities.add(userAccountPrivilege);
		return this;
	}

	public UserPrincipal addPrivilege(Authority authority) {
		this.grantedAuthorities.add(GrantedPermission.builder()
				.authority(authority)
				.principal(this)
				.build());
		return this;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = CollectionsUtility.newHashSet();
		for (GrantedPermission accountPrivilege :this.grantedAuthorities) {
			authorities.add(accountPrivilege.getAuthority());
		}
		return authorities;
	}

	@Transient
	public boolean isSelected(Long id){
		if (null != id){
			return this.getId().equals(id);
		}
		return false;
	}

	public static UserPrincipal valueOf(String ssoId, String password, String email, Authority[] authorities) {
		UserPrincipal instance = UserPrincipal.builder().build();

		instance.setUsername(ssoId);
		instance.setPassword(password);
		instance.setEmail(email);
		instance.setEnabled(Boolean.TRUE);
		for (Authority authority :authorities) {
			instance.addPrivilege(authority);
		}
		return instance;
	}
}