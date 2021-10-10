package net.ecology.auth.service;

import net.ecology.domain.auth.UserAccountProfile;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.exceptions.AuthException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.service.IGenericService;

public interface UserAccountProfileService extends IGenericService<UserAccountProfile, Long> {
  /**
   * Finds the user with the provided name.
   * 
   * @param username The user name
   * @return The user
   * @throws ObjectNotFoundException If no such user exists.
   */
	UserAccountProfile getObject(String property, String username) throws ObjectNotFoundException;

	/**
	 * Create a new user with the supplied details.
	 */
	UserAccountProfile register(UserPrincipal user) throws AuthException;

	/**
	 * Remove the user with the given login name from the system.
	 */
	void deleteUser(String ssoId);

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
	UserAccountProfile confirm(String confirmedEmail) throws AuthException;
}
