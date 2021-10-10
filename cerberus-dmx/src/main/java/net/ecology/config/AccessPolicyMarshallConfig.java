/**
 * 
 */
package net.ecology.config;

/**
 * @author ducbq
 *
 */
public enum AccessPolicyMarshallConfig {
	accessPolicyAntMatcher((short)0),
	accessPolicyAuthority((short)1),
	accessPolicyAuthorityDisplayName((short)2),
	accessPolicySsoId((short)3),
	accessPolicyEmail((short)4),
	accessPolicyFirstName((short)5),
	accessPolicyLastName((short)6)
	;

	private short index;
	public short index() {
		return index;
	}

	public void index(short index) {
		this.index = index;
	}

	private AccessPolicyMarshallConfig(short index) {
		this.index(index);
	}
}