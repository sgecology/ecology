package net.ecology.auth.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.ecology.auth.certificate.TokenAuthenticationService;
import net.ecology.auth.persistence.UserPrincipalPersistence;
import net.ecology.auth.service.AuthorityService;
import net.ecology.auth.service.UserPrincipalService;
import net.ecology.common.BeanUtility;
import net.ecology.common.CommonUtility;
import net.ecology.common.DateTimeUtility;
import net.ecology.domain.auth.AuthorityGroup;
import net.ecology.entity.auth.Authority;
import net.ecology.entity.auth.UserAccountProfile;
import net.ecology.exceptions.AuthException;
import net.ecology.exceptions.EcosExceptionCode;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;


@Service
public class UserPrincipalServiceImpl extends GenericService<UserAccountProfile, Long> implements UserPrincipalService {
	private static final long serialVersionUID = 1174683251205910776L;

	@Inject
	private AuthorityService authorityService;

	@Autowired
  private UserPrincipalPersistence repository;

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private TokenAuthenticationService jwtService;

	@Override
  protected IPersistence<UserAccountProfile, Long> getPersistence() {
      return repository;
  }

	@Override
	public UserAccountProfile get(String username) throws ObjectNotFoundException {
		return repository.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws AuthException {
		logger.debug("Authenticating {}", login);
		String lowercaseLogin = login;//.toLowerCase();
		UserAccountProfile userFromDatabase = repository.findByUsername(login);

		if (null==userFromDatabase)
			throw new AuthException(EcosExceptionCode.ERROR_INVALID_PROFILE, String.format("User %s was not found in the database", lowercaseLogin));

		if (Boolean.FALSE.equals(userFromDatabase.getEnabled()))
			throw new AuthException(EcosExceptionCode.ERROR_PROFILE_INACTIVATE, String.format("User %s is not activated", lowercaseLogin));

		List<GrantedAuthority> grantedAuthorities = userFromDatabase.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(userFromDatabase.getUsername(), userFromDatabase.getPassword(), grantedAuthorities);
	}

	@Override
	public UserDetails loadUserByEmail(String email) {
		UserAccountProfile userFromDatabase = repository.findByEmail(email);
		//TODO: Remove after then
		if (null == userFromDatabase) {
			throw new UsernameNotFoundException(String.format("User with email %s was not found in the database", email));
			//return this.buildDummyUser(email);
		}

		if (!Boolean.TRUE.equals(userFromDatabase.getEnabled()))
				throw new AuthException(String.format("User with email %s is not activated", email));
		
		Collection<? extends GrantedAuthority> authorities = userFromDatabase.getAuthorities();
		return this.buildUserDetails(userFromDatabase, authorities);
	}

	@Override
	public UserAccountProfile register(UserAccountProfile userAccount) throws AuthException {
		UserAccountProfile updatedUserAccount = null;
		UserAccountProfile registrationProfile = null;
		try {
			updatedUserAccount = (UserAccountProfile)BeanUtility.clone(userAccount);
			updatedUserAccount.setRegisteredDate(DateTimeUtility.systemDateTime());

			updatedUserAccount.setPassword(passwordEncoder.encode(updatedUserAccount.getPassword()));
			updatedUserAccount = this.save(updatedUserAccount);

			updatedUserAccount.setToken(jwtService.generateToken(updatedUserAccount));
			updatedUserAccount = this.saveOrUpdate(updatedUserAccount);

			registrationProfile = UserAccountProfile.builder()
					.firstName(updatedUserAccount.getFirstName())
					.lastName(updatedUserAccount.getLastName())
					//.securityAccount(updatedUserAccount)
					.build();
		} catch (Exception e) {
			throw new AuthException(e);
		}
		return registrationProfile;
	}

	/*private boolean hasBeenEncoded(String password) {
		final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z] {53}");

		if (BCRYPT_PATTERN.matcher(password).matches())
			return true;

		return false;
	}*/

	@Override
	public void deleteUser(String username) {
		UserAccountProfile removedObject = this.repository.findByUsername(username);
		if (null != removedObject) {
			this.repository.delete(removedObject);
		}
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean userExists(String username) {
		return (null != this.repository.findByUsername(username));
	}

	@Override
	public int countByLogin(String login) {
		return this.repository.countByUsername(login).intValue();
	}

	@Override
	protected Page<UserAccountProfile> performSearch(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public UserAccount save(UserAccount user) {
		//user.setPassword(virtualEncoder.encode(user.getPassword()));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		repository.save(user);
		return user;
	}*/

	@Override
	public UserAccountProfile getUserAccount(String loginId, String password) throws AuthException {
		UserAccountProfile authenticatedUser = null;
		UserDetails userDetails = null;
		UserAccountProfile repositoryUser = null;
		if (CommonUtility.isEmailAddreess(loginId)){
			repositoryUser = repository.findByEmail(loginId);
		}else{
			repositoryUser = repository.findByUsername(loginId);
		}

		if (null == repositoryUser)
			throw new AuthException(EcosExceptionCode.ERROR_INVALID_PRINCIPAL, "Could not get the user information base on [" + loginId + "]");

		if (!this.passwordEncoder.matches(password, repositoryUser.getPassword()))
			throw new AuthException(EcosExceptionCode.ERROR_INVALID_CREDENTIAL, "Invalid password of the user information base on [" + loginId + "]");

		if (!Boolean.TRUE.equals(repositoryUser.getEnabled()))
			throw new AuthException(EcosExceptionCode.ERROR_PROFILE_INACTIVATE, "Login information is fine but this account did not activated yet. ");

		Collection<? extends GrantedAuthority> authorities = repositoryUser.getAuthorities();
		userDetails = buildUserDetails(repositoryUser, authorities);
		authenticatedUser = repositoryUser;
		//authenticatedUser.setUserDetails(userDetails);
		return authenticatedUser;
	}

	@Override
	public UserAccountProfile getUserAccount(String userToken) throws AuthException {
		UserAccountProfile repositoryUser = null;
		if (CommonUtility.isEmailAddreess(userToken)){
			repositoryUser = repository.findByEmail(userToken);
		}else{
			repositoryUser = repository.findByUsername(userToken);
		}

		if (null==repositoryUser){
			throw new AuthException(EcosExceptionCode.ERROR_INVALID_PRINCIPAL, "Could not get the user information base on [" + userToken + "]");
		}

		return repositoryUser;
	}

	@Override
	public void initializeMasterData() throws AuthException {
		//UserAccount adminUser = null, clientUser = null, user = null;
		Authority clientRoleEntity = null, userRoleEntity = null, adminRoleEntity = null;
		//Setup authorities/roles
		try {
			clientRoleEntity = authorityService.getByName(AuthorityGroup.RoleClient.getCode());
			if (null==clientRoleEntity){
				clientRoleEntity = Authority.builder().name(AuthorityGroup.RoleClient.getCode()).displayName("Client activity. ").build();
				authorityService.save(clientRoleEntity);
			}

			userRoleEntity = authorityService.getByName(AuthorityGroup.RoleUser.getCode());
			if (null==userRoleEntity){
				userRoleEntity = Authority.builder().name(AuthorityGroup.RoleUser.getCode()).displayName("Common activity for normal user. ").build();
				authorityService.saveOrUpdate(userRoleEntity);
			}

			adminRoleEntity = authorityService.getByName(AuthorityGroup.RoleAdmin.getCode());
			if (null==adminRoleEntity){
				adminRoleEntity = Authority.builder().name(AuthorityGroup.RoleAdmin.getCode()).displayName("System Administration. ").build();
				authorityService.saveOrUpdate(adminRoleEntity);
			}

			Set<Authority> adminAuthorities = new HashSet<>();
			adminAuthorities.add(userRoleEntity);
			adminAuthorities.add(clientRoleEntity);
			adminAuthorities.add(adminRoleEntity);

			Set<Authority> clientAuthorities = new HashSet<>();
			clientAuthorities.add(clientRoleEntity);

			Set<Authority> userAuthorities = new HashSet<>();
			userAuthorities.add(userRoleEntity);

		} catch (Exception e) {
			throw new AuthException(e);
		}
	}

	@Override
	public UserAccountProfile confirm(String confirmedEmail) throws AuthException {
		UserAccountProfile confirmUser = repository.findByEmail(confirmedEmail);
		if (null == confirmUser)
			throw new AuthException("The email not found in database: " + confirmedEmail);

		confirmUser.setEnabled(true);
		repository.save(confirmUser);
		return confirmUser;
	}

	private UserDetails buildUserDetails(UserAccountProfile userProfile, Collection<? extends GrantedAuthority> authorities){
		//List<UserAccountPrivilege> authorities = userAccountPrivilegeRepository.findByUserAccount(userProfile);
		//List<GrantedAuthority> grantedAuthorities = userProfile.getAuthorities().stream()
		//		.map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toList());

		List<GrantedAuthority> grantedAuthorities = authorities.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(userProfile.getUsername(), userProfile.getPassword(), grantedAuthorities);
	}

	@Override
	public boolean existsByEmail(String emailAddress) {
		return this.repository.existsByEmail(emailAddress);
	}
}
