package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers;

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

    public DatabaseHandler(String dbName) throws SQLException {
        this.dbName = dbName;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "ASD123");
        connectionProps.put("serverTimezone", "UTC");
        connectionProps.put("sessionTimezone", "UTC");
        String dbURL = "jdbc:mysql://localhost:3306/monthly_spendings";
        connection = DriverManager.getConnection(dbURL, connectionProps);

        String insertQuery = String.format("INSERT INTO %s (DATE, AMOUNT) VALUES (?, ?)", this.dbName);
        insertStatement = connection.prepareStatement(insertQuery);
        String selectQueryInRange = String.format("SELECT * FROM %s WHERE YEAR(DATE)=? AND MONTH(DATE)=?", this.dbName);
        selectStatementInRange = connection.prepareStatement(selectQueryInRange);
        String deleteQuery = String.format("DELETE FROM %s WHERE DATE=? AND AMOUNT=?", this.dbName);
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
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        LocalDate localDate = LocalDate.of(year, month, day);
        insertStatement.setObject(1, localDate);
        insertStatement.setInt(2, amount);
        insertStatement.executeUpdate();

        BankBalanceHandler bankBalanceHandler = new BankBalanceHandler();
        if(dbName.equals("income")){
            bankBalanceHandler.updateBankBalanceByGivenDay(year, month, day, amount);
        }
        else{
            bankBalanceHandler.updateBankBalanceByGivenDay(year, month, day, -amount);
        }
    }

    public void deleteFromDataBaseByGivenDayAndAmount(int year, int month, int day, int amount) throws SQLException{
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        LocalDate localDate = LocalDate.of(year, month, day);
        deleteStatement.setObject(1, localDate);
        deleteStatement.setObject(2, amount);
        deleteStatement.executeUpdate();

        BankBalanceHandler bankBalanceHandler = new BankBalanceHandler();
        if(dbName.equals("income")){
            bankBalanceHandler.updateBankBalanceByGivenDay(year, month, day, -amount);
        }
        else{
            bankBalanceHandler.updateBankBalanceByGivenDay(year, month, day, amount);
        }
    }
}
