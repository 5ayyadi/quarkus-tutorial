package com.api;

import org.junit.jupiter.api.Test;
import com.core.customTypes.Address;

import com.core.network.Network;
import com.core.schemas.request.WithdrawDepositRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigInteger;
import java.net.URL;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class TransactionTest {

    @TestHTTPResource("/transaction")
    URL trxUrl;

    /*
     * {
     * "network": "FtmTestnet",
     * "tokenAddress": "e3249321b47090b2998976397fd17b3ee26bdce6",
     * "walletAddress": "1d006127b22952870f327b30ca370b4af78fb5dc",
     * "amount": 2500000,
     * "userId": 1,
     * "tokenId": 1,
     * "status": "RECEIVED",
     * "trxHash":
     * "0xbb89ca2e2d864393d5d90ee1aa7f1e8a2f2846df0dbc091affcf14b0ff295665"
     * }
     */
    @Test
    public void testPostDeposit() {
        // TODO WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
        Long user_id = 0l;
        Long token_id = 4l;

        WithdrawDepositRequest request = new WithdrawDepositRequest(
                user_id,
                new Address("0x1d006127b22952870f327b30ca370b4af78fb5dc"),
                new Address("0x1d006127b22952870f327b30ca370b4af78fb5dc"),
                new Address("0xe3249321b47090b2998976397fd17b3ee26bdce6"),
                new BigInteger("2500000"),
                Network.FtmTestnet,
                "0xbb89ca2e2d864393d5d90ee1aa7f1e8a2f2846df0dbc091affcf14b0ff295665");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post(trxUrl + "/deposit")
                .then()
                .statusCode(200);
    }

    // @Test
    // public void testGetDeposit() {
    // Long user_id = 1L;

    // given()
    // .queryParam("user_id", user_id)
    // .when().get(trxUrl + "/deposit")
    // .then()
    // .statusCode(200)
    // // .body("id", is(4))
    // .body("address", is("0x1d006127b22952870f327b30ca370b4af78fb5dc"));
    // }

}
