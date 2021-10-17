/*package net.ecology.entity.auth.base;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import net.ecology.framework.entity.Repo;

@MappedSuperclass
public abstract class PrincipalEntity extends Repo implements PrincipalDetails {
	private static final long serialVersionUID = 3241131548383593243L;

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

	@Getter @Setter
	@Column(name = "email", unique = true, nullable=false, length=70)
	private String email;

	@Getter @Setter
	@Column(name = "locked")
	private Boolean locked = false;

	@Getter @Setter
	@Column(name = "enabled")
	private Boolean enabled = false;

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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

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

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
*/