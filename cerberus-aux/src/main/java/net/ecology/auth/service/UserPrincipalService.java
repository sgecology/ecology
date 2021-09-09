package net.ecology.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import net.ecology.entity.auth.UserPrincipal;
import net.ecology.exceptions.NgepAuthException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;
import net.ecology.model.auth.UserAccountProfile;

public interface UserPrincipalService extends IGenericService<UserPrincipal, Long>, UserDetailsService {
    /**
     * Finds the user with the provided name.
     * 
     * @param username The user name
     * @return The user
     * @throws ObjectNotFoundException If no such user exists.
     */
	UserPrincipal get(String userName) throws ObjectNotFoundException;

	/**
	 * Create a new user with the supplied details.
	 */
	UserAccountProfile register(UserPrincipal user) throws NgepAuthException;

	/**
	 * Remove the user with the given login name from the system.
	 */
	void deleteUser(String username);

	/**
	 * Modify the current user's password. This should change the user's password in the
	 * persistent user repository (database, LDAP etc).
	 *
	 * @param oldPassword current password (for re-authentication if required)
	 * @param newPassword the password to change to
	 */
	void changePassword(String oldPassword, String newPassword);

	/**
	 * Check if a user with the email exists in the system.
	 */
	boolean existsByEmail(String emailAddress);

	/**
	 * Check if a user with the supplied login name exists in the system.
	 */
	boolean userExists(String username);

	int countByLogin(String login);

	UserDetails loadUserByEmail(final String email);
	//SecurityAccount save(SecurityAccount user);
	UserPrincipal getUserAccount(String loginId, String password) throws NgepAuthException;
	UserPrincipal getUserAccount(String userToken) throws NgepAuthException;
	UserPrincipal confirm(String confirmedEmail) throws NgepAuthException;
	void initializeMasterData() throws NgepAuthException;
}
