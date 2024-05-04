package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers;

import com.example.monthlySpendingsBackend.envVariableHandler.EnvVariableHandlerSingleton;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class DatabaseHandler {

    protected final PreparedStatement insertStatement;
    private final PreparedStatement selectStatementInRange;
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
        String selectQueryInRange = String.format("SELECT * FROM %s WHERE YEAR(DATE)=? AND MONTH(DATE)=? AND USER_ID=%d", this.dbName, userId);
        selectStatementInRange = connection.prepareStatement(selectQueryInRange);
        String deleteQuery = String.format("DELETE FROM %s WHERE DATE=? AND AMOUNT=? AND USER_ID=%d", this.dbName, userId);
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

    public Map<Date, List<Integer>> getExpensesByGivenMonth(int year, int month) throws SQLException{
        Map<Date, List<Integer>> expensesForGivenMonth = new HashMap<>();
        selectStatementInRange.setInt(1, year);
        selectStatementInRange.setInt(2, month);
        ResultSet results = selectStatementInRange.executeQuery();
        while(results.next()){
            Date date = results.getDate(2);
            int amount = results.getInt(3);
            if(expensesForGivenMonth.containsKey(date)){
                expensesForGivenMonth.get(date).add(amount);
            }
            else{
                List<Integer> initList = new ArrayList<>();
                initList.add(amount);
                expensesForGivenMonth.put(date, initList);
            }
        }
        return expensesForGivenMonth;
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
            //TODO: handle this case with a message to the frontend
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
