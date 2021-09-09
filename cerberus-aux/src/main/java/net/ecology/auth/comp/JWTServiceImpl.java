/**
 * 
 */
package net.ecology.auth.comp;

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
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.entity.auth.base.PrincipalDetails;
import net.ecology.framework.component.ComponentRoot;

/**
 * @author ducbq
 *
 */
@Named
@Component
public class JWTServiceImpl extends ComponentRoot implements JWTService {
	private static final long serialVersionUID = -312627269682252483L;

	private static final String TOKEN_SUBJECT_SEPARATOR = "#";
  // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
  private final String JWT_SECRET = "S1mpl3l#57";

  //Thời gian có hiệu lực của chuỗi jwt
  private static final long JWT_EXPIRATION = 604800000L; // Default is 7 days
  private static final long JWT_EXPIRATION_IN_DECADE = 315532800000L; 
  private static final long JWT_EXPIRATION_INDEFINITE = JWT_EXPIRATION_IN_DECADE*3; //in 30 years

  private String generateRegularToken(PrincipalDetails userDetails, long expirationIn) {
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
  public String generateToken(PrincipalDetails userDetails) {
  	return generateRegularToken(userDetails, JWT_EXPIRATION);
  }

  public String generateIndefiniteToken(PrincipalDetails userDetails) {
  	return generateRegularToken(userDetails, JWT_EXPIRATION_INDEFINITE);
  }

  private String marshall(PrincipalDetails userDetails) {
  	return new StringBuilder(Long.toString(userDetails.getId()))
  			.append(TOKEN_SUBJECT_SEPARATOR)
  			.append(userDetails.getUsername())
  			.toString();
  }

  private PrincipalDetails unmarshall(String source) {
  	PrincipalDetails userDetails = null;
  	if (!source.contains(TOKEN_SUBJECT_SEPARATOR))
  		return null;

  	userDetails = initiateUserDetails();
  	String[] parts = source.split(TOKEN_SUBJECT_SEPARATOR);
  	userDetails.setId(Long.valueOf(parts[0]));
  	userDetails.setUsername(parts[1]);
  	return userDetails;
	}

  private PrincipalDetails initiateUserDetails() {
  	PrincipalDetails userDetails = new UserPrincipal();
  	return userDetails;
  }

  public PrincipalDetails generateAuthenticationDetails(String jWebToken) {
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
      try {
          Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jWebToken);
          return true;
      } catch (MalformedJwtException ex) {
          logger.error("Invalid JWT token");
      } catch (ExpiredJwtException ex) {
          logger.error("Expired JWT token");
      } catch (UnsupportedJwtException ex) {
          logger.error("Unsupported JWT token");
      } catch (IllegalArgumentException ex) {
          logger.error("JWT claims string is empty.");
      }
      return false;
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
		final String REQUEST_HEADER_BEARER_TOKENS = "BearerToken";

		String bearerToken = req.getHeader("Authorization");
		bearerToken = Base64Utils.decode(bearerToken);
    if (bearerToken != null && bearerToken.startsWith(REQUEST_HEADER_BEARER_TOKENS)) {
        return bearerToken.substring(REQUEST_HEADER_BEARER_TOKENS.length(), bearerToken.length());
    }
		return null;
	}
}
