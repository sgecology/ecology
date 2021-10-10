/**
 * 
 */
package net.ecology.config.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import net.ecology.auth.certificate.TokenAuthenticationService;

/**
 * @author ducbq
 *
 */
public class JwtSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
  private TokenAuthenticationService jwtTokenProvider;

  public JwtSecurityConfigurer(TokenAuthenticationService jwtTokenProvider) {
      this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
      JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
      http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
