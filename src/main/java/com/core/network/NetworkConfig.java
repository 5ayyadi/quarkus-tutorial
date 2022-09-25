package com.core.network;

import io.quarkus.resteasy.runtime.vertx.JsonObjectReader;
import org.web3j.abi.datatypes.Address;

import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;



public class NetworkConfig {
    public Address WrappedToken;
    public List<String> RPCList;
    public List<String> webSocketList;

    public NetworkConfig(String filePath) {
        try {
            FileReader file = new FileReader(filePath);

//            JsonReader o =  new JsonObjectReader();
//            JsonArrayReader networksJsonArrayReader = new JsonArrayReader(file);
//            JsonObject o = new JsonObject();
//            JsonObject networksJsonObjects = new JsonOb
//            JsonArray networksJsonValues = new J
//            file.
//            file.read()
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
