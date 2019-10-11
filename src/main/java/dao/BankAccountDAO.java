package dao;

import model.BankAccount;
import db.*;
import model.Currency;
import java.sql.*;

public class BankAccountDAO {
    private static final String BANK_ACCOUNT_TABLE = "bank_account";
    private static final String BANK_ACCOUNT_ID = "account_id";
    private static final String BANK_ACCOUNT_OWNER_NAME = "account_owner_name";
    private static final String BANK_ACCOUNT_BALANCE = "balance";
    private static final String BANK_ACCOUNT_CURRENCY_ID = "currency_id";

    private static BankAccountDAO instance = new BankAccountDAO();
    public static BankAccountDAO getInstance() {
        return instance;
    }

    public BankAccount getBankAccountById(long id) {
        BankAccount bankAccount = null;
        String sqlString =
                "select * from " + BANK_ACCOUNT_TABLE + " ba " +
                        "where ba." + BANK_ACCOUNT_ID + " = ?";
        Connection con = null;
        try {
            con = DataSource.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sqlString);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long account_id = resultSet.getInt(BANK_ACCOUNT_ID);
                bankAccount = new BankAccount(account_id
                        , resultSet.getString(BANK_ACCOUNT_OWNER_NAME)
                        , resultSet.getBigDecimal(BANK_ACCOUNT_BALANCE)
                        , Currency.fromInteger(resultSet.getInt(BANK_ACCOUNT_CURRENCY_ID)));
            }
            con.commit();
        } catch (Exception ex) {
            DBUtils.rollbackTransaction(con);
        } finally {
            DBUtils.closeConnection(con);
        }
        return bankAccount;
    }

    //row level "write lock"
    public BankAccount getBankAccountById_ForUpdate(Connection con, Long id) throws SQLException {
        BankAccount bankAccount = null;
        String sqlString =
                "select * from " + BANK_ACCOUNT_TABLE + " ba " +
                        "where ba." + BANK_ACCOUNT_ID + " = ? " +
                        "for update";
        PreparedStatement preparedStatement = con.prepareStatement(sqlString);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            bankAccount = new BankAccount(resultSet.getLong(BANK_ACCOUNT_ID)
                    , resultSet.getString(BANK_ACCOUNT_OWNER_NAME)
                    , resultSet.getBigDecimal(BANK_ACCOUNT_BALANCE)
                    , Currency.fromInteger(resultSet.getInt(BANK_ACCOUNT_CURRENCY_ID)));
        }
        return bankAccount;
    }

    public int updateBankAccount(Connection con, BankAccount bankAccount) throws SQLException {
        String sqlString =
                "update " + BANK_ACCOUNT_TABLE +
                        " set " +
                        BANK_ACCOUNT_BALANCE + " = ? " +
                        "where " + BANK_ACCOUNT_ID + " = ?";

        PreparedStatement preparedStatement = con.prepareStatement(sqlString);
        preparedStatement.setBigDecimal(1, bankAccount.getBalance());
        preparedStatement.setLong(2, bankAccount.getAccountId());
        return preparedStatement.executeUpdate();
    }

    public BankAccount createBankAccount(BankAccount bankAccount) {
        String sqlString =
                "insert into " + BANK_ACCOUNT_TABLE +
                        " (" +
                        BANK_ACCOUNT_ID + ", " +
                        BANK_ACCOUNT_OWNER_NAME + ", " +
                        BANK_ACCOUNT_BALANCE + ", " +
                        BANK_ACCOUNT_CURRENCY_ID +
                        ") values (?, ?, ?,?)";

        Connection con = null;
        try {
            con = DataSource.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sqlString);
            preparedStatement.setLong(1, bankAccount.getAccountId());
            preparedStatement.setString(2, bankAccount.getAccountOwnerName());
            preparedStatement.setBigDecimal(3, bankAccount.getBalance());
            preparedStatement.setInt(4, bankAccount.getCurrency().ordinal());
            preparedStatement.executeUpdate();
            con.commit();
        } catch (Exception ex) {
            DBUtils.rollbackTransaction(con);
            return null;
        } finally {
            DBUtils.closeConnection(con);
        }
        return bankAccount;
    }
}
