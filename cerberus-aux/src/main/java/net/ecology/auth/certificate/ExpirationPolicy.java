/**
 * 
 */
package net.ecology.auth.certificate;

/**
 * @author ducbq
 *
 */
public enum ExpirationPolicy {
  tokenExpirationOneWeek(10080), // Default is 7 days in minute
  tokenExpirationFourWeeks(10080*4), // Default is 28 days in minute
  tokenExpirationDecade(5256000), // Default is 10 years in minutes
  tokenExpirationIndefinite(tokenExpirationDecade.expiredPolicy *7), //in 70 years

  expirationInMonth(30), 
  expirationInYear(365), 
  expirationInFiveYears(expirationInYear.expiredPolicy*5)
  ;
	private int expiredPolicy;

	public int getExpiredPolicy() {
		return expiredPolicy;
	}

	private ExpirationPolicy(int expirationPolicy) {
		this.expiredPolicy = expirationPolicy;
	}
}
