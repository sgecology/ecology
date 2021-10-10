package net.ecology.auth.service.impl;

import javax.inject.Inject;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.ecology.auth.certificate.TokenAuthenticationService;
import net.ecology.auth.service.AuthorityService;
import net.ecology.auth.service.UserAccountProfileService;
import net.ecology.auth.service.UserPrincipalService;
import net.ecology.domain.auth.UserAccountProfile;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.exceptions.AuthException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class UserAccountProfileServiceImpl extends GenericService<UserAccountProfile, Long> implements UserAccountProfileService {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6033439932741319171L;

	@Inject
	private AuthorityService authorityService;

	@Inject
	private UserPrincipalService userPrincipalService;

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private TokenAuthenticationService jwtService;

	@Inject
	protected UserPrincipalService securityAccountService;

  protected IPersistence<UserAccountProfile, Long> getPersistence() {
      return null;
  }

	@Override
	public UserAccountProfile getObject(String property, String username) throws ObjectNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccountProfile register(UserPrincipal user) throws AuthException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String ssoId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existsByEmail(String emailAddress) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserAccountProfile confirm(String confirmedEmail) throws AuthException {
		// TODO Auto-generated method stub
		return null;
	}
}
