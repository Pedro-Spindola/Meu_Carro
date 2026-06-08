package com.drivenote.it;

import com.drivenote.security.RateLimitFilter;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired(required = false)
    private Optional<RateLimitFilter> rateLimitFilter;

    @BeforeAll
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    void resetRateLimit() {
        if (rateLimitFilter.isPresent()) {
            rateLimitFilter.get().resetBuckets();
        }
    }
}