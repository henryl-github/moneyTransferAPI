package dao;

import db.DBUtils;
import db.DataSource;
import model.Currency;
import model.Transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferDAO {

    private static final String TRANSFER_TABLE = "transfer";
    private static final String TRANSFER_ID = "transfer_id";
    private static final String TRANSFER_FROM_ID = "from_account_id";
    private static final String TRANSFER_TO_ID = "to_account_id";
    private static final String AMOUNT = "amount";
    private static final String CURRENCY_ID = "currency_id";

    private  static TransferDAO instance=new TransferDAO();
    public static TransferDAO getInstance(){
        return instance;
    }

    public Transfer getTransferById(long id) {
        Transfer transfer = null;
        String sqlString =
                "select * from " + TRANSFER_TABLE + " tr " +
                        "where tr." + id + " = ?";
        Connection con = null;
        try {
            con = DataSource.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sqlString);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long transfer_id= resultSet.getLong(TRANSFER_ID);
                transfer = new Transfer(
                        resultSet.getLong(TRANSFER_FROM_ID)
                        , resultSet.getLong(TRANSFER_TO_ID)
                        , resultSet.getBigDecimal(AMOUNT)
                        , Currency.fromInteger(resultSet.getInt(CURRENCY_ID)));
                transfer.setTransferId(transfer_id);
            }
            con.commit();
        } catch (Exception ex) {
            DBUtils.rollbackTransaction(con);
        } finally {
            DBUtils.closeConnection(con);
        }
        return transfer;
    }

    public Transfer createTransaction(Connection con,Transfer transfer) throws SQLException {
        String sqlString =
                "insert into " + TRANSFER_TABLE +
                        " (" +
                        TRANSFER_ID + ", " +
                        TRANSFER_FROM_ID + ", " +
                        TRANSFER_TO_ID + ", " +
                        AMOUNT + ", " +
                        CURRENCY_ID + ", " +
                        ") values (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = con.prepareStatement(sqlString);
        preparedStatement.setLong(1, transfer.getTransferId());
        preparedStatement.setLong(2, transfer.getFromAccountId());
        preparedStatement.setLong(3, transfer.getToAccountId());
        preparedStatement.setBigDecimal(4, transfer.getAmount());
        preparedStatement.setInt(5, transfer.getCurrency().ordinal());
        preparedStatement.executeUpdate();
        return transfer;
    }

}
