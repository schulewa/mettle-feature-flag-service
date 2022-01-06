package com.mettle.mettlefeatureflagservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  public static final String AUTHORITIES_CLAIM_NAME = "roles";

  public static final String ADMIN_ROLE = "ADMIN";
  public static final String USER_ROLE = "USER";

  private static final String NON_ADMIN_USER_1_NAME = "nonAdminUser1";
  private static final String NON_ADMIN_USER_1_PASSWORD = "nonAdmin234";

  private static final String ADMIN_USER_1_NAME = "adminUser1";
  private static final String ADMIN_USER_1_PASSWORD = "admin123";

  private final PasswordEncoder passwordEncoder;

  public WebSecurityConfig(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

//  @Override
//  public void configure(WebSecurity web) throws Exception {
//    web
//        .ignoring()
//        .antMatchers("/login");
//  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/**");
  }

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http
//        .cors()
//        .and()
//        .csrf().disable()
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
//        .authorizeRequests(configurer ->
//            configurer
//              .antMatchers("/error", "/login")
//              .permitAll()
//              .anyRequest()
//              .authenticated()
//        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
//
//    // JWT Validation Configuration
//    http.oauth2ResourceServer()
//        .jwt()
//        .jwtAuthenticationConverter(authenticationConverter());
//  }

  @Bean
  @Override
  protected UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

    UserDetails adminUser1 = User
        .withUsername(ADMIN_USER_1_NAME)
        .authorities(ADMIN_ROLE, USER_ROLE)
        .passwordEncoder(passwordEncoder::encode)
        .password(ADMIN_USER_1_PASSWORD)
        .build();
    manager.createUser(adminUser1);

    UserDetails nonAdminUser1 = User
        .withUsername(NON_ADMIN_USER_1_NAME)
        .authorities(USER_ROLE)
        .passwordEncoder(passwordEncoder::encode)
        .password(NON_ADMIN_USER_1_PASSWORD)
        .build();
    manager.createUser(nonAdminUser1);

    return manager;
  }

  protected JwtAuthenticationConverter authenticationConverter() {
    JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    authoritiesConverter.setAuthorityPrefix("");
    authoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_CLAIM_NAME);

    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
    return converter;
  }

}
