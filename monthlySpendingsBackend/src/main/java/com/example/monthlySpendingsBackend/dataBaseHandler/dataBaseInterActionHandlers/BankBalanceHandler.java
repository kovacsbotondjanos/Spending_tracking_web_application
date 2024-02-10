package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.TimeZone;

public class BankBalanceHandler extends DatabaseHandler{
    private final PreparedStatement selectStatement;
    private final PreparedStatement selectStatementForUpdate;
    private final PreparedStatement updateStatement;

    public BankBalanceHandler() throws SQLException {
        super("BANKBALANCE");
        String selectQuery = String.format("SELECT * FROM %s WHERE DATE<=? ORDER BY DATE DESC", this.dbName);
        this.selectStatement = connection.prepareStatement(selectQuery);
        String selectQueryForUpdate = String.format("SELECT * FROM %s WHERE DATE>=? ORDER BY DATE ASC", this.dbName);
        this.selectStatementForUpdate = connection.prepareStatement(selectQueryForUpdate);
        String updateQuery = String.format("UPDATE %s SET AMOUNT=? WHERE DATE=?", this.dbName);
        this.updateStatement = connection.prepareStatement(updateQuery);
    }

    public int getBankBalanceByGivenDay(int year, int month, int day) throws SQLException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        LocalDate localDate = LocalDate.of(year, month, day);
        selectStatement.setObject(1, localDate);
        ResultSet results = selectStatement.executeQuery();
        if(!results.next()){
            return 0;
        }
        return results.getInt("AMOUNT");
    }
    public void updateBankBalanceByGivenDay(int year, int month, int day, int diff) throws SQLException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        LocalDate localDate = LocalDate.of(year, month, day);
        selectStatementForUpdate.setObject(1, localDate);
        ResultSet results = selectStatementForUpdate.executeQuery();
        results.next();
        if(results.getDate("DATE").toLocalDate().equals(localDate)){
            updateStatement.setObject(2, localDate);
            updateStatement.setInt(1, results.getInt("AMOUNT") + diff);
            updateStatement.executeUpdate();
        }
        else{
            insertStatement.setObject(1, localDate);
            insertStatement.setInt(2, results.getInt("AMOUNT") + diff);
            insertStatement.executeUpdate();
        }
        while(results.next()){
            updateStatement.setObject(2, results.getObject("DATE"));
            updateStatement.setInt(1, results.getInt("AMOUNT") + diff);
            updateStatement.executeUpdate();
        }
    }
}
