
package api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import logic.BankAccountLogic;
import model.*;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class BankAccountAPI {

    @GET
    @Path("{id}")
    public Response getBankAccountById(@PathParam("id") Long id) {
        BankAccount bankAccount;
        bankAccount = BankAccountLogic.getInstance().getBankAccountById(id);

        if (bankAccount == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(bankAccount).build();
    }

    @POST
    public Response createBankAccount(BankAccount bankAccount) {
        BankAccount returnedBankAccount = BankAccountLogic.getInstance().createBankAccount(
                bankAccount.getAccountOwnerName(), bankAccount.getBalance(), Currency.GBP);
        if (returnedBankAccount == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.ok(returnedBankAccount).build();
    }
}