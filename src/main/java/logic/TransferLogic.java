package logic;

import dao.BankAccountDAO;
import dao.TransferDAO;
import db.DBUtils;
import db.DataSource;
import model.BankAccount;
import model.Transfer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicLong;

public class TransferLogic {
    private AtomicLong incrementalCount;

    private TransferLogic() {
        incrementalCount = new AtomicLong(0);
    }

    private static TransferLogic instance = new TransferLogic();

    public static TransferLogic getInstance() {
        return instance;
    }

    public Transfer getTransferById(long id) {
        return TransferDAO.getInstance().getTransferById(id);
    }

    public int transferMoney(Transfer transfer) {
        //invalid transfer
        if (transfer.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0)
            return 400;
        //invalid transfer
        if (transfer.getFromAccountId().equals(transfer.getToAccountId()))
            return 400;


        long fromAccountId = transfer.getFromAccountId();
        long toAccountId = transfer.getToAccountId();

        Connection con = null;
        try {
            con = DataSource.getConnection();

            //lock rows in database in sequential order to avoid deadlock
            BankAccount fromBankAccount = null;
            BankAccount toBankAccount = null;
            if (fromAccountId < toAccountId) {
                fromBankAccount = BankAccountDAO.getInstance().getBankAccountById_ForUpdate(con, fromAccountId);
                toBankAccount = BankAccountDAO.getInstance().getBankAccountById_ForUpdate(con, toAccountId);
            } else {
                toBankAccount = BankAccountDAO.getInstance().getBankAccountById_ForUpdate(con, toAccountId);
                fromBankAccount = BankAccountDAO.getInstance().getBankAccountById_ForUpdate(con, fromAccountId);
            }

            //account not found
            if (fromBankAccount == null || toBankAccount == null)
                return 404;
            //not sufficient amount
            if (fromBankAccount.getBalance().compareTo(transfer.getAmount()) < 0)
                return 406;

            long generatedTransferId = incrementalCount.addAndGet(1);
            transfer.setTransferId(generatedTransferId);

            fromBankAccount.setBalance(fromBankAccount.getBalance().subtract(transfer.getAmount()));
            toBankAccount.setBalance(toBankAccount.getBalance().add(transfer.getAmount()));

            BankAccountDAO.getInstance().updateBankAccount(con, fromBankAccount);
            BankAccountDAO.getInstance().updateBankAccount(con, toBankAccount);

            TransferDAO.getInstance().createTransaction(con, transfer);
            con.commit();
        } catch (Exception ex) {
            DBUtils.rollbackTransaction(con);
            return 400;
        } finally {
            DBUtils.closeConnection(con);
        }
        return 200;
    }
}
