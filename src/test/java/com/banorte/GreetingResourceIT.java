package com.banorte;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import static io.restassured.RestAssured.given;

@QuarkusIntegrationTest
class GreetingResourceIT extends GreetingResourceTest {
    // Execute the same tests but in packaged mode.
    @Test
    void testUnknownEndpoint() {
        given()
            .when().get("/unknown")
            .then()
            .statusCode(404);
    }
}
