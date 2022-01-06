package com.mettle.mettlefeatureflagservice.rest;

import com.mettle.mettlefeatureflagservice.logic.FeatureFlagService;
import com.mettle.mettlefeatureflagservice.rest.model.FeatureFlagDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("feature-flags")
@Slf4j
public class FeatureFlagController {

  private final FeatureFlagService featureFlagService;

  public FeatureFlagController(FeatureFlagService service) {
    featureFlagService = service;
  }

  @GetMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  // accessible by ADMIN and users
  ResponseEntity<List<FeatureFlagDto>> getAll() {
    log.info("Processing request to get all feature flags");
    return new ResponseEntity<>(featureFlagService.getAll(), HttpStatus.OK);
  }

  @GetMapping(value = "/user/{user}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  // accessible by ADMIN and users
  List<FeatureFlagDto> getForUser(@PathVariable String user) {
    log.info("Processing request to get feature flags for user " + user);
    return featureFlagService.getForUser(user);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  //@PreAuthorize("hasAuthority('ADMIN')")
  ResponseEntity<FeatureFlagDto> create(@RequestBody FeatureFlagDto newFeatureFlag) {
    log.info("Processing request to create feature flag [" + newFeatureFlag.toString() + "]");
    return new ResponseEntity<>(featureFlagService.create(newFeatureFlag), HttpStatus.CREATED);
  }

  @PutMapping(value = "/featureflags/{name}/enable", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  //@PreAuthorize("hasAuthority('ADMIN')")
  Boolean enable(@PathVariable String name) {
    log.info("Processing request to enable feature flag " + name);
    return featureFlagService.enable(name);
  }

  @PutMapping(value = "/featureflags/{name}/disable", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  //@PreAuthorize("hasAuthority('ADMIN')")
  Boolean disable(@PathVariable String name) {
    log.info("Processing request to disable feature flag " + name);
    return featureFlagService.disable(name);
  }

}
