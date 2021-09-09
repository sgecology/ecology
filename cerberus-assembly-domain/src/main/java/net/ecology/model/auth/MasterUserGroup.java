/**
 * 
 */
package net.ecology.model.auth;

/**
 * @author bqduc
 *
 */
public enum MasterUserGroup {
	Administrator("Administrator"),
	VpexClient("VpexClient"), 
	User("User")
	;

	private String login;

	private MasterUserGroup(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
