package com.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import com.core.customTypes.Address;
import com.core.network.Network;
import com.core.schemas.request.WithdrawDepositRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigInteger;

@QuarkusTest
public class DepositResourceTest {

    Address ZERO = new Address("0x4c97380af08e1ee1846f00f737c0e1121087fedd");
    Address ONE = new Address("0x1d006127b22952870f327b30ca370b4af78fb5dc");
    Address MASTER = new Address("0x840d6c2705d9669d8ae53cca3dd9e4ada7d655d0");
    Address TOKEN = new Address("0xe3249321b47090b2998976397fd17b3ee26bdce6");
    Network network = Network.FtmTestnet;

    @Test
    public void testGetDeposit() {
        given()
                .queryParams("user_id", 0L)
                .when().get("/transaction/deposit")
                .then()
                .body("address.value", is(ZERO.toString()))
                .statusCode(200);
        given()
                .queryParams("user_id", 1L)
                .when().get("/transaction/deposit")
                .then()
                .body("address.value", is(ONE.toString()))
                .statusCode(200);
    }

    @Test
    public void testPostDeposit() {

        WithdrawDepositRequest wdRequest = new WithdrawDepositRequest(
                0L,
                MASTER,
                ZERO,
                TOKEN,
                new BigInteger("1500000"),
                network,
                "0xbff4adf8111778ea4a98d23c520ba7a198c907f00d110face594dccab737832a"

        );

        given()
                .contentType(ContentType.JSON)
                .body(wdRequest)
                .when().post("/transaction/deposit")
                .then()
                .body("address.value", is(ZERO.getValue()))
                .statusCode(200);

        wdRequest = new WithdrawDepositRequest(
                1L,
                MASTER,
                ONE,
                TOKEN,
                new BigInteger("2500000"),
                network,
                "0xbb89ca2e2d864393d5d90ee1aa7f1e8a2f2846df0dbc091affcf14b0ff295665");

        given()
                .contentType(ContentType.JSON)
                .body(wdRequest)
                .when().post("/transaction/deposit")
                .then()
                .body("address.value", is(ONE.getValue()))
                .statusCode(200);

        wdRequest = new WithdrawDepositRequest(
                1L,
                MASTER,
                ONE,
                TOKEN,
                new BigInteger("1500000"),
                network,
                "0x46d8a9e6495c6c8b901496149966369509e305fca4867b3a04d6dac11d78863c");

        given()
                .contentType(ContentType.JSON)
                .body(wdRequest)
                .when().post("/transaction/deposit")
                .then()
                .body("address.value", is(ONE.getValue()))
                .statusCode(200);

    }
}
