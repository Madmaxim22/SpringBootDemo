package com.example.springbootdemo.autoconfiguration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
    private static final GenericContainer<?> myAppFirst = new GenericContainer<>("devapp:latest")
            .withExposedPorts(8080);
    private static final GenericContainer<?> myAppSecond = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081);
    @Autowired
    TestRestTemplate restTemplate;
    
    @BeforeAll
    public static void setUp() {
        myAppFirst.start();
        myAppSecond.start();
    }
    
    @Test
    void contextLoadDevApp() {
        ResponseEntity<String> entityFirst = restTemplate.getForEntity(
                "http://localhost:" + myAppFirst.getMappedPort(8080) + "/profile", String.class);
        
        assertEquals("Current profile is dev", entityFirst.getBody());
    }
    
    @Test
    void contextLoadProdApp() {
        ResponseEntity<String> entitySecond = restTemplate.getForEntity(
                "http://localhost:" + myAppSecond.getMappedPort(8081) + "/profile", String.class);
        
        assertEquals("Current profile is production", entitySecond.getBody());
    }
}
