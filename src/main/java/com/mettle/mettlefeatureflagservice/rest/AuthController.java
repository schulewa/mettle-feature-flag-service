package com.mettle.mettlefeatureflagservice.rest;

import com.mettle.mettlefeatureflagservice.logic.JwtHelper;
import com.mettle.mettlefeatureflagservice.rest.model.LoginResult;
import com.mettle.mettlefeatureflagservice.rest.model.UserLogonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping
public class AuthController {

  private final JwtHelper jwtHelper;
  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  public AuthController(JwtHelper jwtHelper, UserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {
    this.jwtHelper = jwtHelper;
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping(path = "login", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public LoginResult login(@RequestBody UserLogonDto userLogonDto) {

    log.info("Processing login request for user " + userLogonDto.getUserName());

    UserDetails userDetails;
    try {
      userDetails = userDetailsService.loadUserByUsername(userLogonDto.getUserName());
    } catch (UsernameNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
    }

    if (passwordEncoder.matches(userLogonDto.getPassword(), userDetails.getPassword())) {
      Map<String, String> claims = new HashMap<>();
      claims.put("username", userLogonDto.getUserName());

      String authorities = userDetails.getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.joining(","));
      claims.put("authorities", authorities);
      claims.put("userId", String.valueOf(1));

      String jwt = jwtHelper.createJwtForClaims(userLogonDto.getUserName(), claims);
      return new LoginResult(jwt);
    }

    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
  }
}
