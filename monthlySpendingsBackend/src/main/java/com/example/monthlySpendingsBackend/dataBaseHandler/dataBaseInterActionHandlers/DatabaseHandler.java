package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers;

import com.example.monthlySpendingsBackend.contexts.ApplicationContextProvider;
import com.example.monthlySpendingsBackend.envVariableHandler.EnvVariableHandlerSingleton;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalanceService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.OutgoingService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.UserDetailService;
import org.springframework.context.ApplicationContext;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

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

    public Map<Date, List<Integer>> getExpensesByGivenMonth(int year, int month) throws SQLException{
        Map<Date, List<Integer>> expensesForGivenMonth = new HashMap<>();

        int lastDay = new org.joda.time.LocalDate(year, month, 1).dayOfMonth().withMaximumValue().getDayOfMonth();

        LocalDate startLoc = LocalDate.of(year, month, 1);
        Date start = Date.from(startLoc.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endLoc = LocalDate.of(year, month, lastDay);
        Date end = Date.from(endLoc.atStartOfDay(ZoneId.systemDefault()).toInstant());

        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        OutgoingService outgoingService = context.getBean(OutgoingService.class);
        UserDetailService us = context.getBean(UserDetailService.class);
        BankBalanceService bankBalanceService = context.getBean(BankBalanceService.class);
        CustomUser user = us.getUserById(userId);
        bankBalanceService.updateBankBalance(start, 0, user);

        var expenses = outgoingService.getOutgoingExpenseByUserIdAndTypeBetweenDates(start, end, userId, dbName);
        expenses.forEach(e -> {
            Date date = e.getDate();
            int amount = e.getAmount();
            if(expensesForGivenMonth.containsKey(date)){
                expensesForGivenMonth.get(date).add(amount);
            }
            else{
                List<Integer> initList = new ArrayList<>();
                initList.add(amount);
                expensesForGivenMonth.put(date, initList);
            }
        });
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
