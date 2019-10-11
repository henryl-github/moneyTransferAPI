package unit;

import logic.BankAccountLogic;
import model.BankAccount;
import model.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountLogicTest {

    @Test
    void getTheRightBankAccountById() {
        BankAccountLogic bankAccountLogicInstance = BankAccountLogic.getInstance();
        BankAccount testBankAccount = bankAccountLogicInstance.createBankAccount("Henry",
                BigDecimal.valueOf(100), Currency.GBP);
        BankAccount returnedBankAccount = bankAccountLogicInstance.getBankAccountById(testBankAccount.getAccountId());
        assertEquals(testBankAccount.getAccountId(), returnedBankAccount.getAccountId());
        assertEquals(true, returnedBankAccount.getBalance().compareTo(testBankAccount.getBalance()) == 0);
        assertEquals(testBankAccount.getAccountOwnerName(), returnedBankAccount.getAccountOwnerName());
        assertEquals(testBankAccount.getCurrency(), returnedBankAccount.getCurrency());
    }

    @Test
    void getInvalidAccountByIdShouldReturnNull() {
        BankAccountLogic bankAccountLogicInstance = BankAccountLogic.getInstance();
        long invalidBankAccountId = -1;
        BankAccount returnedBankAccount = bankAccountLogicInstance.getBankAccountById(invalidBankAccountId);
        assertEquals(null, returnedBankAccount);
    }

    @Test
    void CreateAccountSuccessfully() {
        String accountOwnerName = "Henry";
        BigDecimal initialBalance = BigDecimal.valueOf(200);
        Currency currency = Currency.GBP;
        BankAccount returnedBankAccount = BankAccountLogic.getInstance().createBankAccount(accountOwnerName,
                initialBalance, currency);
        assertEquals("Henry", returnedBankAccount.getAccountOwnerName());
        assertEquals(true, returnedBankAccount.getBalance().compareTo(initialBalance) == 0);
        assertEquals(currency, returnedBankAccount.getCurrency());
    }

    @Test
    void CreateAccountWithInitialBalanceUnderZeroFailed() {
        String accountOwnerName = "Henry";
        BigDecimal initialBalance = BigDecimal.valueOf(-100);
        Currency currency = Currency.GBP;
        BankAccount returnedBankAccount = BankAccountLogic.getInstance().createBankAccount(accountOwnerName,
                initialBalance, currency);
        assertEquals(null, returnedBankAccount);
    }

}
