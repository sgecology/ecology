/**
 * 
 */
package net.ecology.auth.certificate;

import javax.servlet.http.HttpServletRequest;

import net.ecology.entity.auth.base.PrincipalDetails;

/**
 * @author ducbq
 *
 */
public interface TokenAuthenticationService {
  /**
  * Generate the JWeb token base on the authentication detail. The generated token will be expired in 7 days from generated time
  * @param authenticationDetails The authentication detail object to be generated. 
  * @return String Generated JWeb token.
  */
  String generateToken(PrincipalDetails authenticationDetails);

  /**
  * Generate the JWeb token base on the authentication detail. The generated token will be expired in 20 years from generated time, can be considered as indefinite. 
  * @param authenticationDetails The authentication detail object to be generated. 
  * @return String Generated JWeb token.
  */
  String generateIndefiniteToken(PrincipalDetails authenticationDetails);

  /**
  * Generate the JWeb token base on the authentication detail. The generated token will be expired in 7 days from generated time
  * @param jWebToken The web token string to be generated to authentication details object. 
  * @return AuthenticationDetails Generated AuthenticationDetails.
  */
  PrincipalDetails resolve(String jWebToken);

  /**
  * Validate the token is indefinite or not
  * @param jWebToken The web token string to be checked. 
  * @return Boolean True if corrected, otherwise false.
  */
  boolean isIndefiniteToken(String jWebToken);

  /**
  * Validate the JWeb token is corrected or not. 
  * @param jWebToken The web token string to be validated. 
  * @return Boolean True if corrected, otherwise false.
  */
  boolean validateToken(String jWebToken);

  String resolveToken(HttpServletRequest req);
}
