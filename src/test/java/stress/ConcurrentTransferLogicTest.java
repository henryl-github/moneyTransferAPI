package stress;

import logic.BankAccountLogic;
import logic.TransferLogic;
import model.BankAccount;
import model.Currency;
import model.Transfer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentTransferLogicTest {
    private static final int NUM_OF_ITERATIONS = 10000;
    private static final int INITIAL_VALUE = 1000000;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @RepeatedTest(5)
    void transferMoneyConcurrentlyBetweenTwoAccountsThenBalanceShouldStayUnchanged() throws InterruptedException {
        BankAccount bankAccount1 = BankAccountLogic.getInstance().createBankAccount("Henry",
                BigDecimal.valueOf(INITIAL_VALUE), Currency.GBP);
        BankAccount bankAccount2 = BankAccountLogic.getInstance().createBankAccount("Henry2",
                BigDecimal.valueOf(INITIAL_VALUE), Currency.GBP);

        TransferLogic transferLogicInstance = TransferLogic.getInstance();
        Transfer transfer1 = new Transfer(bankAccount1.getAccountId(), bankAccount2.getAccountId(), BigDecimal.valueOf(1), Currency.GBP);
        Transfer transfer2 = new Transfer(bankAccount2.getAccountId(), bankAccount1.getAccountId(), BigDecimal.valueOf(1), Currency.GBP);

        List<Thread> allThreads = new ArrayList<>();
        class TransferThread1 extends Thread {
            public void run() {
                for (int i = 0; i < NUM_OF_ITERATIONS; i++) {
                    transferLogicInstance.transferMoney(transfer1);
                }
            }
        }

        class TransferThread2 extends Thread {
            public void run() {
                for (int i = 0; i < NUM_OF_ITERATIONS; i++) {
                    transferLogicInstance.transferMoney(transfer2);
                }
            }
        }

        Thread newThread1 = new TransferThread1();
        allThreads.add(newThread1);

        Thread neaThread2 = new TransferThread2();
        allThreads.add(neaThread2);

        for (Thread thread : allThreads)
            thread.start();

        for (Thread thread : allThreads)
            thread.join();

        BankAccount account1 = BankAccountLogic.getInstance().getBankAccountById(bankAccount1.getAccountId());
        BankAccount account2 = BankAccountLogic.getInstance().getBankAccountById(bankAccount2.getAccountId());

        assertEquals(INITIAL_VALUE, account1.getBalance().intValue());
        assertEquals(INITIAL_VALUE, account2.getBalance().intValue());
    }


    @Test
    @RepeatedTest(5)
    void transferMoneyConcurrentlyBetweenThreeAccountsInCircleThenBalanceShouldStayUnchanged() throws InterruptedException {
        BankAccount bankAccount1 = BankAccountLogic.getInstance().createBankAccount("Henry",
                BigDecimal.valueOf(INITIAL_VALUE), Currency.GBP);
        BankAccount bankAccount2 = BankAccountLogic.getInstance().createBankAccount("Henry2",
                BigDecimal.valueOf(INITIAL_VALUE), Currency.GBP);
        BankAccount bankAccount3 = BankAccountLogic.getInstance().createBankAccount("Henry3",
                BigDecimal.valueOf(INITIAL_VALUE), Currency.GBP);

        TransferLogic transferLogicInstance = TransferLogic.getInstance();
        Transfer transfer1 = new Transfer(bankAccount1.getAccountId(), bankAccount2.getAccountId(), BigDecimal.valueOf(1), Currency.GBP);
        Transfer transfer2 = new Transfer(bankAccount2.getAccountId(), bankAccount3.getAccountId(), BigDecimal.valueOf(1), Currency.GBP);
        Transfer transfer3= new Transfer(bankAccount3.getAccountId(), bankAccount1.getAccountId(), BigDecimal.valueOf(1), Currency.GBP);

        List<Thread> allThreads = new ArrayList<>();
        class TransferThread1 extends Thread {
            public void run() {
                for (int i = 0; i < NUM_OF_ITERATIONS; i++) {
                    transferLogicInstance.transferMoney(transfer1);
                }
            }
        }

        class TransferThread2 extends Thread {
            public void run() {
                for (int i = 0; i < NUM_OF_ITERATIONS; i++) {
                    transferLogicInstance.transferMoney(transfer2);
                }
            }
        }

        class TransferThread3 extends Thread {
            public void run() {
                for (int i = 0; i < NUM_OF_ITERATIONS; i++) {
                    transferLogicInstance.transferMoney(transfer3);
                }
            }
        }
        Thread newThread1 = new TransferThread1();
        allThreads.add(newThread1);

        Thread neaThread2 = new TransferThread2();
        allThreads.add(neaThread2);

        Thread neaThread3 = new TransferThread3();
        allThreads.add(neaThread3);

        for (Thread thread : allThreads)
            thread.start();

        for (Thread thread : allThreads)
            thread.join();

        BankAccount account1 = BankAccountLogic.getInstance().getBankAccountById(bankAccount1.getAccountId());
        BankAccount account2 = BankAccountLogic.getInstance().getBankAccountById(bankAccount2.getAccountId());
        BankAccount account3 = BankAccountLogic.getInstance().getBankAccountById(bankAccount3.getAccountId());

        assertEquals(INITIAL_VALUE, account1.getBalance().intValue());
        assertEquals(INITIAL_VALUE, account2.getBalance().intValue());
        assertEquals(INITIAL_VALUE, account3.getBalance().intValue());
    }


    @Test
    @RepeatedTest(5)
    void transferMoneyConcurrentlyBetweenThreeAccountsThenBalanceShouldBeExpected() throws InterruptedException {
        BankAccount bankAccount1 = BankAccountLogic.getInstance().createBankAccount("Henry",
                BigDecimal.valueOf(INITIAL_VALUE), Currency.GBP);
        BankAccount bankAccount2 = BankAccountLogic.getInstance().createBankAccount("Henry2",
                BigDecimal.valueOf(INITIAL_VALUE), Currency.GBP);
        BankAccount bankAccount3 = BankAccountLogic.getInstance().createBankAccount("Henry3",
                BigDecimal.valueOf(INITIAL_VALUE), Currency.GBP);

        TransferLogic transferLogicInstance = TransferLogic.getInstance();
        Transfer transfer1 = new Transfer(bankAccount1.getAccountId(), bankAccount2.getAccountId(), BigDecimal.valueOf(1), Currency.GBP);
        Transfer transfer2 = new Transfer(bankAccount2.getAccountId(), bankAccount3.getAccountId(), BigDecimal.valueOf(1), Currency.GBP);
        Transfer transfer3= new Transfer(bankAccount1.getAccountId(), bankAccount3.getAccountId(), BigDecimal.valueOf(1), Currency.GBP);

        List<Thread> allThreads = new ArrayList<>();
        class TransferThread1 extends Thread {
            public void run() {
                for (int i = 0; i < NUM_OF_ITERATIONS; i++) {
                    transferLogicInstance.transferMoney(transfer1);
                }
            }
        }

        class TransferThread2 extends Thread {
            public void run() {
                for (int i = 0; i < NUM_OF_ITERATIONS; i++) {
                    transferLogicInstance.transferMoney(transfer2);
                }
            }
        }

        class TransferThread3 extends Thread {
            public void run() {
                for (int i = 0; i < NUM_OF_ITERATIONS; i++) {
                    transferLogicInstance.transferMoney(transfer3);
                }
            }
        }
        Thread newThread1 = new TransferThread1();
        allThreads.add(newThread1);

        Thread neaThread2 = new TransferThread2();
        allThreads.add(neaThread2);

        Thread neaThread3 = new TransferThread3();
        allThreads.add(neaThread3);

        for (Thread thread : allThreads)
            thread.start();

        for (Thread thread : allThreads)
            thread.join();

        BankAccount account1 = BankAccountLogic.getInstance().getBankAccountById(bankAccount1.getAccountId());
        BankAccount account2 = BankAccountLogic.getInstance().getBankAccountById(bankAccount2.getAccountId());
        BankAccount account3 = BankAccountLogic.getInstance().getBankAccountById(bankAccount3.getAccountId());

        assertEquals(INITIAL_VALUE-2*NUM_OF_ITERATIONS, account1.getBalance().intValue());
        assertEquals(INITIAL_VALUE, account2.getBalance().intValue());
        assertEquals(INITIAL_VALUE+2*NUM_OF_ITERATIONS, account3.getBalance().intValue());
    }
}