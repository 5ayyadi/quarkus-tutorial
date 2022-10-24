package com.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import com.core.customTypes.Address;
import com.core.network.Network;
import com.core.schemas.request.TokenRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TokenResource {

    Address TEST_TOKEN_ADDRESS = new Address("0xe3249321b47090b2998976397fd17b3ee26bdce6");
    Network network = Network.FtmTestnet;

    @Test
    public void testPostToken() {

        TokenRequest validToken = new TokenRequest(TEST_TOKEN_ADDRESS, network);
        given()
                .contentType(ContentType.JSON)
                .body(validToken)
                .when().post("/token")
                .then()
                .body("symbol", is("TST"))
                .statusCode(201);
    }

    @Test
    public void testGetToken() {

        given()
                .queryParams("symbol", "TST")
                .when().get("/token")
                .then()
                // .body("symbol", any(ETH_USDC))
                .statusCode(200);
        given()
                .queryParams("address", TEST_TOKEN_ADDRESS)
                .when().get("/token")
                .then()
                .statusCode(200);

    }

}
