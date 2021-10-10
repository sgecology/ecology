/**
 * 
 */
package net.ecology.auth.certificate;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.ecology.common.DateTimeUtility;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.entity.auth.base.PrincipalDetails;

/**
 * @author ducbq
 *
 */
@Component
public class TokenServiceHelper {
	public static final String TOKEN_SUBJECT_SEPARATOR = "#";
  private final String JWT_SECRET = "S1mpl3l#57";

	public String generateToken(PrincipalDetails principalDetails, TokenExpirationPolicy tokenExpirationPolicy, TokenGenerationPolicy generationPolicy) {
		Date issuedDate = DateTimeUtility.systemTime();
		Date expiryDate = DateTimeUtility.add(issuedDate, Calendar.MINUTE, tokenExpirationPolicy.getExpirationPolicy());
    return Jwts.builder()
               .setSubject(buildSubject(principalDetails, generationPolicy, TOKEN_SUBJECT_SEPARATOR))
               .setIssuedAt(issuedDate)
               .setExpiration(expiryDate)
               .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
               .compact();
	}

	public PrincipalDetails resolve(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(JWT_SECRET)
        .parseClaimsJws(token)
        .getBody();

		return resovleToken(claims.getSubject());
	}

  private String buildSubject(PrincipalDetails userDetails, TokenGenerationPolicy generationPolicy, String separator) {
  	StringBuilder subject = null;
  	switch (generationPolicy) {
  	case SsoIdObjId:
  		subject = new StringBuilder()
			.append(Long.toString(userDetails.getId()))
			.append(separator)
			.append(userDetails.getUsername());
  		break;
  	case SsoIdEmail:
  		subject = new StringBuilder()
			.append(userDetails.getUsername())
			.append(separator)
			.append(userDetails.getEmail());
  		break;
  	case SsoIdName:
  		subject = new StringBuilder()
			.append(userDetails.getUsername())
			.append(separator)
			.append(userDetails.name());
  		break;
  	case EmailName:
  		subject = new StringBuilder()
			.append(userDetails.getEmail())
			.append(separator)
			.append(userDetails.name());
  		break;
  	}

  	subject
  		.append(separator)
    	.append(generationPolicy.name());

  	return subject.toString();
  }

  private PrincipalDetails resovleToken(String token) {
  	PrincipalDetails userDetails = null;
  	if (!token.contains(TOKEN_SUBJECT_SEPARATOR))
  		return null;

  	userDetails = initiateUserDetails();
  	String[] tokenParts = token.split(TOKEN_SUBJECT_SEPARATOR);
  	//Last part are generation policy
  	switch (TokenGenerationPolicy.valueOf(tokenParts[tokenParts.length-1])) {
  	case SsoIdObjId:
    	userDetails.setId(Long.valueOf(tokenParts[0]));
    	userDetails.setUsername(tokenParts[1]);
  		break;
  	case SsoIdEmail:
    	userDetails.setUsername(tokenParts[0]);
    	userDetails.setEmail(tokenParts[1]);
  		break;
  	case SsoIdName:
    	userDetails.setUsername(tokenParts[0]);
    	userDetails.name(tokenParts[1]);
  		break;
  	case EmailName:
    	userDetails.setEmail(tokenParts[0]);
    	userDetails.name(tokenParts[1]);
  		break;
  	}

  	return userDetails;
	}

  private PrincipalDetails initiateUserDetails() {
  	return UserPrincipal.builder().build();
  }
}