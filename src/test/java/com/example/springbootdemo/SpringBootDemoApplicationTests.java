package com.example.springbootdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootDemoApplicationTests {
    
    @Autowired
    TestRestTemplate restTemplate;
    
    @Container
    private static final GenericContainer<?> myAppFirst =
            new GenericContainer<>("devapp:latest").withExposedPorts(8080);
    @Container
    private static final GenericContainer<?> myAppSecond =
            new GenericContainer<>("prodapp:latest").withExposedPorts(8081);
    @Test
    void contextLoadDevApp() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity(
                "http://localhost:" + myAppFirst.getMappedPort(8080) + "/profile", String.class);
        assertEquals("Current profile is dev", forEntity.getBody());
    }
    
    @Test
    void contextLoadProdApp() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity(
                "http://localhost:" + myAppSecond.getMappedPort(8081) + "/profile", String.class);
        assertEquals("Current profile is production", forEntity.getBody());
    }
    
}
