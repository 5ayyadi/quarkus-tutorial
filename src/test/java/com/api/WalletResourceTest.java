package com.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import com.core.schemas.request.WalletCreationRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class WalletResourceTest {

    @Test
    public void testCreateWallet() {
        WalletCreationRequest walletRequest = new WalletCreationRequest(0L);
        given()
                .contentType(ContentType.JSON)
                .body(walletRequest)
                .when().post("/wallet")
                .then()

                .body("address.value", is(
                        "0x4c97380af08e1ee1846f00f737c0e1121087fedd"))
                .statusCode(201);

        walletRequest = new WalletCreationRequest(1L);
        given()
                .contentType(ContentType.JSON)
                .body(walletRequest)
                .when().post("/wallet")
                .then()
                .body("address.value", is("0x1d006127b22952870f327b30ca370b4af78fb5dc"))
                .statusCode(201);

    }

}
