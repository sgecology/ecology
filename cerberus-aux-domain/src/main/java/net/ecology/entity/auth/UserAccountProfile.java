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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.DateTimeUtility;
import net.ecology.domain.UserAccountType;
import net.ecology.domain.model.DateTimePatterns;
import net.ecology.entity.base.UserAccountDetails;
import net.ecology.framework.entity.Repo;
import net.ecology.global.GlobalConfiguredVariables;
import net.ecology.global.GlobeConstants;

/**
 * A user. UserProfile
 * 
 * @author bqduc
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "aut_user_account_profile")
@ToString(exclude = { "grantedAuthorities" })
@EqualsAndHashCode(callSuper = true)
public class UserAccountProfile extends Repo implements UserAccountDetails {
	private static final long serialVersionUID = -1003313430910524298L;

	@Getter @Setter
	//@Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
	@Column(name="sso_id", unique = true, nullable=false, length=50)
	private String username;

	@JsonIgnore
	@Getter @Setter
	@Column(name = "password", nullable=false, length=160)
	private String password;

	@Getter @Setter
	@Column(name = "encrypted_alg", length=30)
	private String encryptedAlg;

  @Setter @Getter
  @Column(name="first_name", length=50)
	private String firstName;

  @Setter @Getter
	@Column(name="last_name", length=70)
	private String lastName;

  @Getter @Setter
	@Column(name = "email", unique = true, nullable=false, length=120)
	private String email;

	@Builder.Default
	@Getter @Setter
	@Column(name = "locked")
	private Boolean locked = false;

	@Builder.Default
	@Getter @Setter
	@Column(name = "enabled")
	private Boolean enabled = false;

	@Builder.Default
	@Getter @Setter
	@Column(name = "visible")
	private Boolean visible = false;
	
	@Getter @Setter
	@Column(name = "enabled_date")
	private Date enabledDate;

	@Getter @Setter
	@Column(name = "registered_date")
	private Date registeredDate;
	
	@Getter @Setter
	@Column(name = "expiration_policy")
	private Short expirationPolicy;

	@JsonIgnore
	@Getter @Setter
	@Column(name = "token", length=450)
	private String token;

	@Setter @Getter
	@Builder.Default
	@LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy="principal", cascade=CascadeType.ALL)
  private List<GrantedPermission> grantedAuthorities = CollectionsUtility.newList();

	@Transient
	@Builder.Default
	@Setter @Getter
	private List<AccessPolicy> accessPolicies = CollectionsUtility.newList();
	
	@Setter @Getter
	@Column(name = "info", columnDefinition="text")
	private String info;

	@Setter @Getter
	@Column(name = "account_type")
  @Enumerated(EnumType.ORDINAL)
	private UserAccountType accountType;	

	@Transient
	@Setter @Getter
	private Authentication authentication;

	@Setter @Getter
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(name="picture")
  private byte[] picture;

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return (this.locked==false);
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Transient
	public String getDisplayIssueDate() {
		return DateTimeUtility.dateToString(this.getEnabledDate(), DateTimePatterns.ddMMyyyy_SLASH.getDateTimePattern());
	}

	public UserAccountProfile addPrivilege(GrantedPermission userAccountPrivilege) {
		this.grantedAuthorities.add(userAccountPrivilege);
		return this;
	}

	public UserAccountProfile addPrivilege(Authority authority) {
		this.grantedAuthorities.add(GrantedPermission.builder()
				.authority(authority)
				.principal(this)
				.build());
		return this;
	}

	public UserAccountProfile addPrivileges(Authority[] authorities) {
		for (Authority authority :authorities) {
			this.grantedAuthorities.add(GrantedPermission.builder()
					.authority(authority)
					.principal(this)
					.build());
		}
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

	public static UserAccountProfile valueOf(String ssoId, String password, String email, Authority[] authorities, String firstName, String lastName) {
		UserAccountProfile instance = UserAccountProfile.builder()
				.firstName(firstName)
				.lastName(lastName)
				.username(ssoId)
				.password(password)
				.email(email)
				.enabled(Boolean.TRUE)
				.build()
				.addPrivileges(authorities);
		return instance;
	}

	@Transient
	public String getDisplayRoles() {
		StringBuilder displayRoles = new StringBuilder();
		Iterator<? extends GrantedPermission> itr = this.grantedAuthorities.iterator();
		Authority currentAuthority = null;
		while (itr.hasNext()) {
			currentAuthority = ((GrantedPermission)itr.next()).getAuthority();
			displayRoles.append(currentAuthority.getName());
			if (itr.hasNext()) {
				displayRoles.append(GlobeConstants.SEPARATOR_SEMICOLON);
			}
		}
		return displayRoles.toString();
	}

	@Transient
	public String getDisplayName() {
		return new StringBuilder(this.firstName)
				.append(GlobalConfiguredVariables.namePartSeparator)
				.append(this.lastName)
				.toString();
	}
}