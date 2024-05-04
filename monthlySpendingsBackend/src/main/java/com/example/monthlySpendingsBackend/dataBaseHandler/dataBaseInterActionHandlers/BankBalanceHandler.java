package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers;

import com.example.monthlySpendingsBackend.envVariableHandler.EnvVariableHandlerSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.TimeZone;

public class BankBalanceHandler extends DatabaseHandler{
    private final PreparedStatement selectStatement;
    private final PreparedStatement selectStatementForUpdate;
    private final PreparedStatement updateStatement;

    public BankBalanceHandler(Connection connection, Long userId) throws SQLException {
        super("BANKBALANCE", connection, userId);
        String selectQuery = String.format("SELECT * FROM %s WHERE DATE<=? AND USER_ID=%d ORDER BY DATE DESC", this.dbName, userId);
        this.selectStatement = connection.prepareStatement(selectQuery);
        String selectQueryForUpdate = String.format("SELECT * FROM %s WHERE DATE>=? AND USER_ID=%d ORDER BY DATE ASC", this.dbName, userId);
        this.selectStatementForUpdate = connection.prepareStatement(selectQueryForUpdate);
        String updateQuery = String.format("UPDATE %s SET AMOUNT=? WHERE DATE=? AND USER_ID=%d", this.dbName, userId);
        this.updateStatement = connection.prepareStatement(updateQuery);
    }

    public int getBankBalanceByGivenDay(int year, int month, int day) throws SQLException {
        TimeZone.setDefault(TimeZone.getTimeZone(EnvVariableHandlerSingleton.getTimeZone()));
        LocalDate localDate = LocalDate.of(year, month, day);
        selectStatement.setObject(1, localDate);
        ResultSet results = selectStatement.executeQuery();
        if(!results.next()){
            return 0;
        }
        return results.getInt("AMOUNT");
    }
    public void updateBankBalanceByGivenDay(int year, int month, int day, int diff) throws SQLException {
        TimeZone.setDefault(TimeZone.getTimeZone(EnvVariableHandlerSingleton.getTimeZone()));
        LocalDate localDate = LocalDate.of(year, month, day);
        selectStatementForUpdate.setObject(1, localDate);
        ResultSet results = selectStatementForUpdate.executeQuery();
        if(results.next() && results.getDate("DATE").toLocalDate().equals(localDate)){
            updateStatement.setObject(2, localDate);
            updateStatement.setInt(1, results.getInt("AMOUNT") + diff);
            updateStatement.executeUpdate();
        }
        else{
            int amount = getBankBalanceByGivenDay(year, month, day);
            insertStatement.setObject(1, localDate);
            insertStatement.setInt(2, amount + diff);
            insertStatement.executeUpdate();
        }
        while(results.next()){
            updateStatement.setObject(2, results.getObject("DATE"));
            updateStatement.setInt(1, results.getInt("AMOUNT") + diff);
            updateStatement.executeUpdate();
        }
    }
}
