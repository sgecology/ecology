/**
 * 
 */
package net.ecology.auth.certificate;

/**
 * @author ducbq
 *
 */
public enum TokenExpirationPolicy {
  expirationOneWeek(10080), // Default is 7 days in minute
  expirationFourWeeks(10080*4), // Default is 28 days in minute
  expirationDecade(5256000), // Default is 10 years in minutes
  expirationIndefinite(expirationDecade.expirationPolicy *7); //in 70 years

	private int expirationPolicy;

	public int getExpirationPolicy() {
		return expirationPolicy;
	}

	private TokenExpirationPolicy(int expirationPolicy) {
		this.expirationPolicy = expirationPolicy;
	}
}
