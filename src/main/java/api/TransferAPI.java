package api;

import logic.TransferLogic;
import model.Transfer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class TransferAPI {
    @POST()
    public Response createTransfer(Transfer transfer) {
        TransferLogic transferLogic = TransferLogic.getInstance();
        int result = transferLogic.transferMoney(transfer);
        if (result == 200)
            return Response.ok().build();
        else if (result == 400) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if (result == 404) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (result == 406)
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("{id}")
    public Response getTransferById(@PathParam("id") Long id) {
        Transfer transfer = TransferLogic.getInstance().getTransferById(id);
        if (transfer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(transfer).build();
    }
}
