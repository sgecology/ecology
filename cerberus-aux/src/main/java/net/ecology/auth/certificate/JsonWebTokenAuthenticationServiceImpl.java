/**
 * 
 */
package net.ecology.auth.certificate;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import net.ecology.common.Base64Utils;
import net.ecology.common.StringUtilities;
import net.ecology.entity.auth.UserAccountProfile;
import net.ecology.entity.base.UserAccountDetails;
import net.ecology.framework.component.BasisComp;

/**
 * @author ducbq
 *
 */
@Named
@Component
public class JsonWebTokenAuthenticationServiceImpl extends BasisComp implements TokenAuthenticationService {
	private static final long serialVersionUID = 2203563860083028287L;

	private static final String TOKEN_SUBJECT_SEPARATOR = "#";
  // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
  private final String JWT_SECRET = "S1mpl3l#57";

  //Thời gian có hiệu lực của chuỗi jwt
  private static final long JWT_EXPIRATION = 604800000L; // Default is 7 days
  private static final long JWT_EXPIRATION_IN_DECADE = 315532800000L; 
  private static final long JWT_EXPIRATION_INDEFINITE = JWT_EXPIRATION_IN_DECADE*3; //in 30 years

  private String generateRegularToken(UserAccountDetails userDetails, long expirationIn) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationIn);
    // Tạo chuỗi json web token từ id của user.
    return Jwts.builder()
               .setSubject(marshall(userDetails))
               .setIssuedAt(now)
               .setExpiration(expiryDate)
               .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
               .compact();
  }

  // Tạo ra jwt từ thông tin user
  public String generateToken(UserAccountDetails userDetails) {
  	return generateRegularToken(userDetails, JWT_EXPIRATION);
  }

  public String generateIndefiniteToken(UserAccountDetails userDetails) {
  	return generateRegularToken(userDetails, JWT_EXPIRATION_INDEFINITE);
  }

  private String marshall(UserAccountDetails userDetails) {
  	return new StringBuilder(Long.toString(userDetails.getId()))
  			.append(TOKEN_SUBJECT_SEPARATOR)
  			.append(userDetails.getUsername())
  			.toString();
  }

  private UserAccountDetails unmarshall(String source) {
  	UserAccountDetails userDetails = null;
  	if (!source.contains(TOKEN_SUBJECT_SEPARATOR))
  		return null;

  	userDetails = initiateUserDetails();
  	String[] parts = source.split(TOKEN_SUBJECT_SEPARATOR);
  	userDetails.setId(Long.valueOf(parts[0]));
  	userDetails.setUsername(parts[1]);
  	return userDetails;
	}

  private UserAccountDetails initiateUserDetails() {
  	UserAccountDetails userDetails = new UserAccountProfile();
  	return userDetails;
  }

  public UserAccountDetails resolve(String jWebToken) {
    Claims claims = Jwts.parser()
        .setSigningKey(JWT_SECRET)
        .parseClaimsJws(jWebToken)
        .getBody();

    return unmarshall(claims.getSubject());
  }

  // Lấy thông tin user từ jwt
  /*
  public Long getUserIdFromJWT(String token) {
      Claims claims = Jwts.parser()
                          .setSigningKey(JWT_SECRET)
                          .parseClaimsJws(token)
                          .getBody();

      return Long.parseLong(claims.getSubject());
  }
  */

  public boolean validateToken(String jWebToken) {
  	boolean validatedToken = false;
  	try {
          Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jWebToken);
          validatedToken = true;
      } catch (MalformedJwtException ex) {
          logger.error("Invalid JWT token");
      } catch (ExpiredJwtException ex) {
          logger.error("Expired JWT token");
      } catch (UnsupportedJwtException ex) {
          logger.error("Unsupported JWT token");
      } catch (IllegalArgumentException ex) {
          logger.error("JWT claims string is empty.");
      }
      return validatedToken;
  }

	@Override
	public boolean isIndefiniteToken(String jWebToken) {
    Claims claims = Jwts.parser()
        .setSigningKey(JWT_SECRET)
        .parseClaimsJws(jWebToken)
        .getBody();

    Calendar issuedDate = Calendar.getInstance();
    issuedDate.setTime(claims.getIssuedAt());

    Calendar expiredDate = Calendar.getInstance();
    expiredDate.setTime(claims.getExpiration());

    return (expiredDate.getTimeInMillis()-issuedDate.getTimeInMillis())==JWT_EXPIRATION_INDEFINITE;
	}

	@Override
	public String resolveToken(HttpServletRequest req) {
		final String[] REQUEST_HEADER_BEARER_TOKENS = new String[] {"Bearer ", "BearerToken"};

		String bearerToken = req.getHeader("Authorization");
		String actualToken = "";
		for (String requestHeaderBearerToken :REQUEST_HEADER_BEARER_TOKENS) {
			if (bearerToken.startsWith(requestHeaderBearerToken)){
				actualToken = bearerToken.substring(requestHeaderBearerToken.length(), bearerToken.length());
				break;
			}
		}

		if (StringUtilities.isBase64(actualToken)) {
			actualToken = Base64Utils.decode(actualToken);
		}

		return actualToken;
	}
}
