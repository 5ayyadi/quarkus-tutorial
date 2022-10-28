package com.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import com.core.customTypes.Address;
import com.core.models.TransactionStatus;
import com.core.models.TransactionType;
import com.core.network.Network;
import com.core.schemas.request.TransferRequest;
import com.core.schemas.request.WithdrawDepositRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigInteger;

@QuarkusTest
public class TransferResourceTest {

    @Test
    public void testPostTransfer() {
        TransferRequest validToken = new TransferRequest(0L, 1L, 53L, new BigInteger("100"), TransactionType.TRANSFER,
                TransactionStatus.CONFIRMED);
        given()
                .contentType(ContentType.JSON)
                .body(validToken)
                .when().post("/transfer")
                .then()
                // .body("address.value", is(ZERO.toString()))
                .statusCode(200);
    }

}