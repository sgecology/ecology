/**
 * 
 */
package net.ecology.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import net.ecology.auth.certificate.TokenAuthenticationService;
import net.ecology.entity.base.UserAccountDetails;

/**
 * @author ducbq
 *
 */
public class JwtTokenFilter extends GenericFilterBean {
	private static Logger logger = LogManager.getLogger(JwtTokenFilter.class);
	private TokenAuthenticationService jwtTokenProvider;

	public JwtTokenFilter(TokenAuthenticationService jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		Authentication authentication = null;
		UserAccountDetails authenticationDetails = null;
		try {
			//HttpServletRequest request = (HttpServletRequest)req;
//			System.out.println(request.getHeader("authorization"));
			String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
			if (token != null && jwtTokenProvider.validateToken(token)) {
				authenticationDetails = jwtTokenProvider.resolve(token);
				authentication = new UsernamePasswordAuthenticationToken(authenticationDetails.getUsername(), authenticationDetails.getPassword(), authenticationDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			filterChain.doFilter(req, res);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
