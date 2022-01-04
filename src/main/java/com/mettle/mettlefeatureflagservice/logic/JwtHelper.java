package com.mettle.mettlefeatureflagservice.logic;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtHelper {

  private final RSAPrivateKey privateKey;
  private final RSAPublicKey publicKey;

  public JwtHelper(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
    this.privateKey = privateKey;
    this.publicKey = publicKey;
  }

  public String createJwtForClaims(String subject, Map<String, String> claims) {
    ZonedDateTime expiresAt = ZonedDateTime.now();
    expiresAt.plus(1, ChronoUnit.HOURS);

//    Calendar calendar = Calendar.getInstance();
//    calendar.setTimeInMillis(Instant.now().toEpochMilli());
//    calendar.add(Calendar.DATE, 10000);

    JWTCreator.Builder jwtBuilder = JWT.create().withSubject(subject);

    // Add claims
    claims.forEach(jwtBuilder::withClaim);

    // Add expiredAt and etc
    return jwtBuilder
        .withNotBefore(new Date())
//        .withExpiresAt(calendar.getTime())
        .withExpiresAt(new Date(expiresAt.toInstant().toEpochMilli()))
        .sign(Algorithm.RSA256(publicKey, privateKey));
  }
}
