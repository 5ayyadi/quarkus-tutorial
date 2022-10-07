package com.gs;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import com.core.models.wallet.Wallet;
import com.core.schemas.request.WalletCreationRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.Matcher;

@QuarkusTest
public class ResourceTest {
    // private ;

    @Test
    public void testWalletEndpoint() {
        WalletCreationRequest validWallet = new WalletCreationRequest(100000023456L);
        given()
                .contentType(ContentType.JSON)
                .body(validWallet)
                .when().post("/wallet")
                .then()
                .statusCode(201);
    }

}