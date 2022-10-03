package com.gs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.core.models.TransactionStatus;
import com.core.schemas.request.WithdrawDepositRequest;

@Path("/transaction")
public class WithdrawDepositResource {
    
    @Path("/withdraw")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdraw(WithdrawDepositRequest request) {
        // create a transaction
        // add transaction to database
        // change status to pending
        request.status = TransactionStatus.PENDING;
        return Response.status(Status.OK)/* .entity(transaction)*/.build();
    }
    
}
