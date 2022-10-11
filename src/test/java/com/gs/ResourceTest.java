package com.gs;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import com.core.network.Network;
import com.core.schemas.request.TokenRequest;
import com.core.schemas.request.WalletCreationRequest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.expect;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigInteger;

import javax.validation.constraints.NotEmpty;

import org.hamcrest.Matcher;

@QuarkusTest
public class ResourceTest {
        // private ;

        @Test
        public void testWalletEndpoint() {
                WalletCreationRequest validWallet = new WalletCreationRequest(1003456L);
                given()
                                .contentType(ContentType.JSON)
                                .body(validWallet)
                                .when().post("/wallet")
                                .then()
                                .statusCode(201);
        }

        @Test
        public void testPostToken() {
                String ETH_USDC = "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48";
                TokenRequest validWallet = new TokenRequest(ETH_USDC, Network.EthereumLocal);
                given()
                                .contentType(ContentType.JSON)
                                .body(validWallet)
                                .when().post("/token")
                                .then()
                                .statusCode(201);
        }

        @Test
        public void testGetToken() {
                String ETH_USDC = "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48";
                String ETH_USDC_S = "USDC";
                given()
                                .queryParams("symbol", ETH_USDC_S)
                                .when().get("/token")
                                .then()
                                .statusCode(200);
                given()
                                .queryParams("address", ETH_USDC)
                                .when().get("/token")
                                .then()
                                // .body(NotEmpty())
                                .statusCode(200);

        }

        // @Test
        // public void testDepositEndpoint() {
        // String ETH_USDC = "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48";
        // WithdrawDepositRequest validWallet = new WithdrawDepositRequest(
        // 0L, "4c97380af08e1ee1846f00f737c0e1121087fedd", ETH_USDC, new
        // BigInteger("150"), Network.EthereumLocal);
        // given()
        // .contentType(ContentType.JSON)
        // .body(validWallet)
        // .when().post("/transaction/deposit")
        // .then()
        // .statusCode(200);
        // }

}