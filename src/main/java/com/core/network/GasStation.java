package com.core.network;

import java.math.BigInteger;

import org.web3j.crypto.RawTransaction;

import com.core.models.TrxReceipt;
import com.core.network.TransactionGeneration.RawTransactionAndExtraInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;

public class GasStation {
    public Double nativeTokenPrice;
    public BigInteger gasPrice;
    public Network network;

    public GasStation(Network network) {
        this.network = network;
        this.populatePrice();
    }

    private double calculatePrice() {
        try {
            String url = String.format(
                    "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=usd",
                    network.value.coingeckoId);
            String response = ApiClient.getRequest(url);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jt = mapper.readTree(response);
            return jt.get(network.value.coingeckoId).get("usd").doubleValue();
        } catch (Exception e) {
            Log.error(e);
            return 0;
        }
    }

    private void populatePrice() {
        try {
            this.gasPrice = (network.w3.ethGasPrice().send().getGasPrice());
            this.nativeTokenPrice = this.calculatePrice();
        } catch (Exception e) {
            Log.errorf("at Price constructor: %s", e);
        }
    }
}
