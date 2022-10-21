package com.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.core.network.GasStation;
import com.core.network.Network;

@Path("/price")
public class PriceResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response gasPrice(@QueryParam("network") Network network) {
        GasStation price = new GasStation(network);
        return Response.status(Status.OK).entity(price).build();
    }

}
