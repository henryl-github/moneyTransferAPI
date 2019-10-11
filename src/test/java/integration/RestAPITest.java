package integration;

import application.MoneyTransferApp;
import logic.BankAccountLogic;
import model.BankAccount;
import model.Currency;
import model.Transfer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestAPITest {

    private static final String BASE_URL = MoneyTransferApp.BASE_URI;
    private static final int INITIAL_AMOUNT = 1000000;
    private static final String TRANSFER_URL = "/transfer";
    private static final String BANK_ACCOUNT_URL = "/accounts";
    private static WebTarget target;

    @BeforeAll
    public static void beforeAll() {
        MoneyTransferApp.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(BASE_URL);
    }

    @AfterAll
    public static void afterAll() {
        MoneyTransferApp.shutDownServer();
    }


    private static Entity from(Transfer transfer) {
        return Entity.entity(transfer, MediaType.valueOf(MediaType.APPLICATION_JSON));
    }

    private static Entity from(BankAccount bankAccount) {
        return Entity.entity(bankAccount, MediaType.valueOf(MediaType.APPLICATION_JSON));
    }

    //BankAccount API
    @Test
    void getBankAccountByIdSuccessfully() {
        BankAccount testBankAccount = BankAccountLogic.getInstance().createBankAccount("Henry",
                BigDecimal.valueOf(INITIAL_AMOUNT), Currency.GBP);
        long accountId = testBankAccount.getAccountId();
        String GET_BANK_ACCOUNT_BY_ID_PATH = "id";
        Response response = target.path(BANK_ACCOUNT_URL + "/{" + GET_BANK_ACCOUNT_BY_ID_PATH + "}")
                .resolveTemplate("id", accountId)
                .request().get();
        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    @Test
    void getBankAccountByIdFailed() {
        long accountId = -1;
        String GET_BANK_ACCOUNT_BY_ID_PATH = "id";
        Response response = target.path(BANK_ACCOUNT_URL + "/{" + GET_BANK_ACCOUNT_BY_ID_PATH + "}")
                .resolveTemplate("id", -1)
                .request().get();
        assertEquals(Response.Status.NOT_FOUND, response.getStatusInfo().toEnum());
    }

    @Test
    void createBankAccountSuccessfully() {
        BankAccount bankAccount = new BankAccount(0, "Henry", BigDecimal.ZERO, Currency.GBP);
        Response response = target.path(BANK_ACCOUNT_URL)
                .request()
                .post(from(bankAccount));
        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
        BankAccount returnBankAccount = response.readEntity(BankAccount.class);
        assertNotEquals(bankAccount.getAccountId(), returnBankAccount.getAccountId());
    }
    @Test
    void createAccountWithInitialBalanceUnderZeroFailed() {
        BankAccount bankAccount = new BankAccount(0, "Henry",BigDecimal.valueOf(-100), Currency.GBP);
        Response response = target.path(BANK_ACCOUNT_URL)
                .request()
                .post(from(bankAccount));
        assertEquals(Response.Status.BAD_REQUEST, response.getStatusInfo().toEnum());
    }


    //Transfer API
    @Test
    void transferSuccessfullyShouldReturnOKCode200() {
        BankAccount fromBankAccount = BankAccountLogic.getInstance().createBankAccount("Henry",
                BigDecimal.valueOf(200), Currency.GBP);
        BankAccount toBankAccount = BankAccountLogic.getInstance().createBankAccount("Henry2",
                BigDecimal.valueOf(0), Currency.GBP);
        Transfer transfer = new Transfer(fromBankAccount.getAccountId(),
                toBankAccount.getAccountId(), BigDecimal.valueOf(100), Currency.GBP);
        Response response = target.path(TRANSFER_URL)
                .request()
                .post(from(transfer));
        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    @Test
    void transferNegativeAmountOfMoneyShouldReturnStatusBAD_Request() {
        Transfer negativeAmountOfMoneyTransfer = new Transfer(0, 1, BigDecimal.valueOf(-100), Currency.GBP);
        Response response = target.path(TRANSFER_URL)
                .request()
                .post(from(negativeAmountOfMoneyTransfer));
        assertEquals(Response.Status.BAD_REQUEST, response.getStatusInfo().toEnum());
    }

    @Test
    void transferToSameAccountShouldReturnStatusBAD_Request() {
        BankAccount newBankAccount = BankAccountLogic.getInstance().createBankAccount("Henry",
                BigDecimal.valueOf(200), Currency.GBP);
        Transfer negativeAmountOfMoneyTransfer = new Transfer(newBankAccount.getAccountId(), newBankAccount.getAccountId(), BigDecimal.valueOf(-100), Currency.GBP);
        Response response = target.path(TRANSFER_URL)
                .request()
                .post(from(negativeAmountOfMoneyTransfer));
        assertEquals(Response.Status.BAD_REQUEST, response.getStatusInfo().toEnum());
    }


}
