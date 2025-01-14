package io.github.wesleyosantos91.api.v1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import io.github.wesleyosantos91.Application;
import io.github.wesleyosantos91.api.v1.request.PersonRequest;
import io.github.wesleyosantos91.api.v1.response.PersonResponse;
import io.github.wesleyosantos91.configuration.AppBeansConfig;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@Sql(value = "/load-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(classes = {Application.class, AppBeansConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class PersonControllerIntegrationTest {

    @LocalServerPort
    Integer port;

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        FixtureFactoryLoader.loadTemplates("io.github.wesleyosantos91.utils.fixture");
    }

    @Test
    @DisplayName("[integration] - should created one person and return id 2")
    void create() {
        PersonRequest personRequest = Fixture.from(PersonRequest.class).gimme("create");
        ResponseEntity<PersonResponse> response =
                restTemplate.postForEntity("http://localhost:" + port + "/v1/persons", personRequest, PersonResponse.class);

        PersonResponse personResponse = response.getBody();
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2L, personResponse.getId());
    }

    @Test
    @DisplayName("[integration] - should return a person with id equals 1")
    void getById() {
        ResponseEntity<PersonResponse> response = restTemplate.getForEntity("http://localhost:" + port + "/v1/persons/{id}", PersonResponse.class, 1);
        PersonResponse personResponse = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, personResponse.getId());
    }

    @Test
    @DisplayName("[integration] - should return a list is not empty")
    void find() {
        ResponseEntity<PersonResponse[]> response = restTemplate.getForEntity("http://localhost:" + port + "/v1/persons", PersonResponse[].class);
        List<PersonResponse> personResponses = List.of(Objects.requireNonNull(response.getBody()));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(personResponses.isEmpty());
    }

    @Test
    @DisplayName("[integration] - should update one person and return email change 'wesleyosantos91@gmail.com' ")
    void update() {
        PersonRequest personRequest = Fixture.from(PersonRequest.class).gimme("update");
        ResponseEntity<PersonResponse> response = restTemplate.exchange("http://localhost:" + port + "/v1/persons/1", HttpMethod.PUT, new HttpEntity<>(personRequest), PersonResponse.class);
        PersonResponse personResponse = response.getBody();
        assertEquals("wesleyosantos91@gmail.com", personResponse.getEmail());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("[integration] - should delete one person with id 1")
    void delete() {
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/v1/persons/1", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }
}