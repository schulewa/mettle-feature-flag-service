package com.mettle.mettlefeatureflagservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mettle.mettlefeatureflagservice.logic.FeatureFlagService;
import com.mettle.mettlefeatureflagservice.model.dao.FeatureFlagEntityDao;
import com.mettle.mettlefeatureflagservice.rest.model.FeatureFlagDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FeatureFlagControllerIT {

  private static final String ADMIN_USER = "Admin";
  private static final String NON_ADMIN_USER_1 = "User1";
  private static final String NON_ADMIN_USER_2 = "User2";

  TestRestTemplate restTemplate = new TestRestTemplate();

  @LocalServerPort
  private Integer port;

  @Autowired
  private FeatureFlagEntityDao dao;

  private ObjectMapper objectMapper;

  @BeforeEach()
  public void beforeEachTest() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

//  @WithMockUser
  @Test
  @Order(1)
  public void test_1_given_no_flags_when_getAll_is_executed_return_empty_list() {
    logStartTest("TEST 1");
    dao.deleteAll();
    ResponseEntity<List<FeatureFlagDto>> response = restTemplate.exchange(constructUri_getAll(), HttpMethod.GET,
                                                                          constructHttpEntity(null),
                                                                          new ParameterizedTypeReference<>() {});
    List<FeatureFlagDto> allFlags = response.getBody();
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertTrue(allFlags.isEmpty());
    logEndTest("TEST 1");
  }

  @Test
  @Order(2)
  public void test_2_given_valid_non_global_flag_when_create_is_executed_return_saved_flag() {
    logStartTest("TEST 2");
    // given
    FeatureFlagDto toBeSaved = new FeatureFlagDto(false, "NonGlobalFlag", NON_ADMIN_USER_1);

    // when
    ResponseEntity<FeatureFlagDto> response = restTemplate.postForEntity(constructUri(), constructHttpEntity(toBeSaved), FeatureFlagDto.class);

    // then
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    FeatureFlagDto saved = response.getBody();
    Assertions.assertFalse(saved.isEnabled());
    Assertions.assertEquals(toBeSaved.isGlobal(), saved.isGlobal());
    Assertions.assertEquals(toBeSaved.getName(), saved.getName());
    Assertions.assertEquals(toBeSaved.getOwner(), saved.getOwner());

    logEndTest("TEST 2");
  }

  @Test
  @Order(3)
  public void test_3_given_valid_global_flag_when_create_is_executed_return_saved_flag() {
    logStartTest("TEST 3");

    // given
    FeatureFlagDto toBeSaved = new FeatureFlagDto(true, "GlobalFlag", NON_ADMIN_USER_2);

    // when
    ResponseEntity<FeatureFlagDto> response = restTemplate.postForEntity(constructUri(), constructHttpEntity(toBeSaved), FeatureFlagDto.class);

    // then
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    FeatureFlagDto saved = response.getBody();
    Assertions.assertFalse(saved.isEnabled());
    Assertions.assertEquals(toBeSaved.isGlobal(), saved.isGlobal());
    Assertions.assertEquals(toBeSaved.getName(), saved.getName());
    Assertions.assertEquals(toBeSaved.getOwner(), saved.getOwner());

    logEndTest("TEST 3");
  }

  @Test
  @Order(4)
  public void test_4_given_existing_flags_when_getAll_is_executed_return_non_empty_list() {
    logStartTest("TEST 4");

    // given
    dao.deleteAll();
    FeatureFlagDto toBeSaved1 = new FeatureFlagDto(true, "GlobalFlag1", NON_ADMIN_USER_1);
    FeatureFlagDto toBeSaved2 = new FeatureFlagDto(true, "GlobalFlag2", NON_ADMIN_USER_2);

    ResponseEntity<FeatureFlagDto> response1 = restTemplate.postForEntity(constructUri(), constructHttpEntity(toBeSaved1), FeatureFlagDto.class);
    ResponseEntity<FeatureFlagDto> response2 = restTemplate.postForEntity(constructUri(), constructHttpEntity(toBeSaved2), FeatureFlagDto.class);

    // when
    ResponseEntity<List<FeatureFlagDto>> response = restTemplate.exchange(constructUri_getAll(),
                                                                          HttpMethod.GET,
                                                                          constructHttpEntity(null),
                                                                          new ParameterizedTypeReference<>() {});
    List<FeatureFlagDto> allFlags = response.getBody();
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(2, allFlags.size());

    logEndTest("TEST 4");
  }

  @Test
  @Order(5)
  public void test_5_given_existing_flags_when_getForUser_is_executed_return_non_empty_list() {
    logStartTest("TEST 5");

    // given
    dao.deleteAll();
    FeatureFlagDto toBeSaved5a = new FeatureFlagDto(true, "GlobalFlag5a", NON_ADMIN_USER_2);
    FeatureFlagDto toBeSaved5b = new FeatureFlagDto(true, "GlobalFlag5b", NON_ADMIN_USER_2);
    FeatureFlagDto toBeSaved5c = new FeatureFlagDto(true, "GlobalFlag5c", NON_ADMIN_USER_2);

    // when
    ResponseEntity<FeatureFlagDto> response5a = restTemplate.postForEntity(constructUri(), constructHttpEntity(toBeSaved5a), FeatureFlagDto.class);
    ResponseEntity<FeatureFlagDto> response5b = restTemplate.postForEntity(constructUri(), constructHttpEntity(toBeSaved5b), FeatureFlagDto.class);
    ResponseEntity<FeatureFlagDto> response5c = restTemplate.postForEntity(constructUri(), constructHttpEntity(toBeSaved5c), FeatureFlagDto.class);

    // then
    Assertions.assertEquals(HttpStatus.CREATED, response5a.getStatusCode());
    Assertions.assertEquals(HttpStatus.CREATED, response5b.getStatusCode());
    Assertions.assertEquals(HttpStatus.CREATED, response5c.getStatusCode());

    ResponseEntity<List<FeatureFlagDto>> response = restTemplate.exchange(constructUri_getForUser(NON_ADMIN_USER_2),
                                                                          HttpMethod.GET,
                                                                          constructHttpEntity(null),
                                                                          new ParameterizedTypeReference<>() {});
    List<FeatureFlagDto> allFlags = Optional.ofNullable(response.getBody()).orElse(new ArrayList<>());

    allFlags.forEach(e -> System.out.println(e.toString()));

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(3, allFlags.size());

    logEndTest("TEST 5");

  }

  @Test
  @Order(6)
  public void test_6_given_existing_flag_when_create_is_executed_return_error() {
    logStartTest("TEST 6");

    // given existing feature flag
    FeatureFlagDto toBeSaved5b = new FeatureFlagDto(true, "GlobalFlag5b", NON_ADMIN_USER_2);

    // when
    ResponseEntity<FeatureFlagDto> response = restTemplate.postForEntity(constructUri(), constructHttpEntity(toBeSaved5b), FeatureFlagDto.class);

    // then
    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    logEndTest("TEST 6");
  }

  private String constructUri_getForUser(String user) {
    return constructUri() + "/user/" + user;
  }
  private String constructUri_getAll() {
    return constructUri() + "/all";
  }

  /**
   * Method <CODE>constructUri</CODE> is used for PUT and POST requests.
   * @return a String representing the url for the request
   */
  private String constructUri() {
    return UriComponentsBuilder
        .newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("feature-flags")
        .toUriString();
  }

  private HttpHeaders constructHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    return headers;
  }

  private HttpEntity<FeatureFlagDto> constructHttpEntity(FeatureFlagDto toBeSaved) {
    HttpHeaders headers = constructHttpHeaders();
    return new HttpEntity<>(toBeSaved, headers);
  }

  private void logStartTest(String testName) {
    System.out.println("INFO: Starting test " + testName + " at " + ZonedDateTime.now());
  }

  private void logEndTest(String testName) {
    System.out.println("INFO: Ending test " + testName + " at " + ZonedDateTime.now());
  }
}
