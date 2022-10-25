package com.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import com.core.network.Network;
import com.core.schemas.request.MasterWalletRequest;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class MasterWalletResourceTest {

    @Test
    public void testCreateWallet() {
        MasterWalletRequest request = new MasterWalletRequest(2L,Network.FtmTestnet);
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/MasterWallet/create")
                .then()
                // .body("address.value", is(
                //         "0x4c97380af08e1ee1846f00f737c0e1121087fedd"))
                .statusCode(201);

    }

}