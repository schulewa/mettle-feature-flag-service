package com.mettle.mettlefeatureflagservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@EntityScan(basePackages = "com.mettle.mettlefeatureflagservice.model.entity")
public class MettleFeatureFlagServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MettleFeatureFlagServiceApplication.class, args);
  }

}
