package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers;

import com.example.monthlySpendingsBackend.envVariableHandler.EnvVariableHandlerSingleton;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DatabaseHandler {

    protected final PreparedStatement insertStatement;
    private final PreparedStatement deleteStatement;
    protected final Connection connection;
    protected final String dbName;
    protected final Long userId;

    public DatabaseHandler(String dbName, Connection connection, Long userId) throws SQLException {
        this.dbName = dbName;
        this.connection = connection;
        this.userId = userId;

        String insertQuery = String.format("INSERT INTO %s (DATE, AMOUNT, USER_ID) VALUES (?, ?, ?)", this.dbName);
        insertStatement = connection.prepareStatement(insertQuery);
        String deleteQuery = String.format("DELETE FROM %s WHERE DATE=? AND AMOUNT=? AND USER_ID=%d", this.dbName, userId);
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

    public void insertIntoDataBaseByGivenDay(int year, int month, int day, int amount) throws SQLException{
        TimeZone.setDefault(TimeZone.getTimeZone(EnvVariableHandlerSingleton.getTimeZone()));
        LocalDate localDate = LocalDate.of(year, month, day);
        insertStatement.setObject(1, localDate);
        insertStatement.setInt(2, amount);
        insertStatement.setLong(3, userId);
        insertStatement.executeUpdate();

        BankBalanceHandler bankBalanceHandler = new BankBalanceHandler(connection, userId);
        if(dbName.equals("INCOME")){
            bankBalanceHandler.updateBankBalanceByGivenDay(year, month, day, amount);
        }
        else{
            bankBalanceHandler.updateBankBalanceByGivenDay(year, month, day, -amount);
        }
    }

    public void deleteFromDataBaseByGivenDayAndAmount(int year, int month, int day, int amount) throws SQLException{
        TimeZone.setDefault(TimeZone.getTimeZone(EnvVariableHandlerSingleton.getTimeZone()));
        LocalDate localDate = LocalDate.of(year, month, day);
        deleteStatement.setObject(1, localDate);
        deleteStatement.setInt(2, amount);
        int rowNum = deleteStatement.executeUpdate();
        if(rowNum == 0){
            return;
        }

        BankBalanceHandler bankBalanceHandler = new BankBalanceHandler(connection, userId);
        if(dbName.equals("INCOME")){
            bankBalanceHandler.updateBankBalanceByGivenDay(year, month, day, -amount);
        }
        else{
            bankBalanceHandler.updateBankBalanceByGivenDay(year, month, day, amount);
        }
    }
}
