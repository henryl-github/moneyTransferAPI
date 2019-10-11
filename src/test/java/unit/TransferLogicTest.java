package unit;

import application.MoneyTransferApp;
import logic.BankAccountLogic;
import logic.TransferLogic;
import model.BankAccount;
import model.Currency;
import model.Transfer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferLogicTest {

    @Test
    void transferNegativeAmountOfMoneyShouldReturnBADRequestCode400() {
        Transfer negativeAmountOfMoneyTransfer = new Transfer(0, 1, BigDecimal.valueOf(-100), Currency.GBP);
        int responseCode = TransferLogic.getInstance().transferMoney(negativeAmountOfMoneyTransfer);
        assertEquals(400, responseCode);
    }

    @Test
    void transferZeroAmountOfMoneyShouldReturnBADRequestCode400() {
        Transfer negativeAmountOfMoneyTransfer = new Transfer(0, 1, BigDecimal.valueOf(0), Currency.GBP);
        int responseCode = TransferLogic.getInstance().transferMoney(negativeAmountOfMoneyTransfer);
        assertEquals(400, responseCode);
    }

    @Test
    void transferFromInvalidAccountShouldReturnBADRequestCode404() {
        Transfer negativeAmountOfMoneyTransfer = new Transfer(-1, 1, BigDecimal.valueOf(100), Currency.GBP);
        int responseCode = TransferLogic.getInstance().transferMoney(negativeAmountOfMoneyTransfer);
        assertEquals(404, responseCode);
    }

    @Test
    void transferToInvalidAccountShouldReturnBADRequestCode400() {
        Transfer negativeAmountOfMoneyTransfer = new Transfer(1, 1, BigDecimal.valueOf(100), Currency.GBP);
        int responseCode = TransferLogic.getInstance().transferMoney(negativeAmountOfMoneyTransfer);
        assertEquals(400, responseCode);
    }

    @Test
    void transferToSameAccountShouldReturnBADRequestCode400() {
        BankAccount fromBankAccount = BankAccountLogic.getInstance().createBankAccount("Henry",
                BigDecimal.valueOf(50), Currency.GBP);
        Transfer negativeAmountOfMoneyTransfer = new Transfer(fromBankAccount.getAccountId(), fromBankAccount.getAccountId(), BigDecimal.valueOf(100), Currency.GBP);
        int responseCode = TransferLogic.getInstance().transferMoney(negativeAmountOfMoneyTransfer);
        assertEquals(400, responseCode);
    }

    @Test
    void transferWithNotEnoughMoneyShouldReturnBADRequestCode406() {
        BankAccount fromBankAccount = BankAccountLogic.getInstance().createBankAccount("Henry",
                BigDecimal.valueOf(50), Currency.GBP);
        BankAccount toBankAccount = BankAccountLogic.getInstance().createBankAccount("Henry2",
                BigDecimal.valueOf(0), Currency.GBP);

        Transfer negativeAmountOfMoneyTransfer = new Transfer(fromBankAccount.getAccountId(),
                toBankAccount.getAccountId(), BigDecimal.valueOf(100), Currency.GBP);
        int responseCode = TransferLogic.getInstance().transferMoney(negativeAmountOfMoneyTransfer);
        assertEquals(406, responseCode);
    }


    @Test
    void transferSuccessfullyShouldReturnBADRequestCode200() {
        BankAccount fromBankAccount = BankAccountLogic.getInstance().createBankAccount("Henry",
                BigDecimal.valueOf(200), Currency.GBP);
        BankAccount toBankAccount = BankAccountLogic.getInstance().createBankAccount("Henry2",
                BigDecimal.valueOf(0), Currency.GBP);

        Transfer transfer = new Transfer(fromBankAccount.getAccountId(),
                toBankAccount.getAccountId(), BigDecimal.valueOf(100), Currency.GBP);
        int responseCode = TransferLogic.getInstance().transferMoney(transfer);
        assertEquals(200, responseCode);
    }

}
