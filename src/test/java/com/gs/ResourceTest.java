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

        // String ETH_USDC_S = "USDC";
        // String ETH_USDC = "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48";
        String ETH_USDC_S = "WFTM";
        String ETH_USDC = "0x07b9c47452c41e8e00f98ac4c075f5c443281d2a";
        Network network = Network.FtmTestnet;

        @Test
        public void testWalletEndpoint() {
                WalletCreationRequest validWallet = new WalletCreationRequest(0L);
                given()
                                .contentType(ContentType.JSON)
                                .body(validWallet)
                                .when().post("/wallet")
                                .then()

                                // .body("address", is(
                                // "<{typeAsString=address,
                                // value=0x4c97380af08e1ee1846f00f737c0e1121087fedd}>"))
                                // .body("address", is(new A("0x4c97380af08e1ee1846f00f737c0e1121087fedd")))

                                .statusCode(201);
                validWallet = new WalletCreationRequest(1L);
                given()
                                .contentType(ContentType.JSON)
                                .body(validWallet)
                                .when().post("/wallet")
                                .then()
                                // .body("address", is("0x1d006127b22952870f327b30ca370b4af78fb5dc"))
                                .statusCode(201);
                validWallet = new WalletCreationRequest(2003456L);
                given()
                                .contentType(ContentType.JSON)
                                .body(validWallet)
                                .when().post("/wallet")
                                .then()
                                // .body("address", is("0x16453187cff8f60de1d8361c4662a6d94cf55535"))
                                .statusCode(201);
        }

        // @Test
        // public void testPostToken() {

        //         TokenRequest validToken = new TokenRequest(ETH_USDC, network);
        //         System.out.println(validToken);
        //         given()
        //                         .contentType(ContentType.JSON)
        //                         .body(validToken)
        //                         .when().post("/token")
        //                         .then()
        //                         .statusCode(201);
        // }

        @Test
        public void testGetToken() {

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