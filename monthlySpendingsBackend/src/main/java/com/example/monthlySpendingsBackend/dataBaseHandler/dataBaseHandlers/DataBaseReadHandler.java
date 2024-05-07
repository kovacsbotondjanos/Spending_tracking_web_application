package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers.BankBalanceHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers.DatabaseHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.DailyStatisticRecord;
import com.example.monthlySpendingsBackend.envVariableHandler.EnvVariableHandlerSingleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

public class DataBaseReadHandler {
    private final int year;
    private final int month;
    private final Long userId;
    private Map<Date, List<Integer>> groceries;
    private Map<Date, List<Integer>> commute;
    private Map<Date, List<Integer>> extra;
    private Map<Date, List<Integer>> rent;
    private Map<Date, List<Integer>> income;
    private final int lastDay;
    private final Connection connection;

    public static Map<Integer, DailyStatisticRecord> DataBaseRead(String year, String month, Long userId) throws SQLException {
        return (new DataBaseReadHandler(year, month, userId)).getDailyStatisticRecordsByMonthByOnlyOneQuery();
    }

    private DataBaseReadHandler(String year, String month, Long userId) throws DateTimeException, NumberFormatException, SQLException{
        this.year = Integer.parseInt(year);
        this.month = Integer.parseInt(month);
        this.userId = userId;

        Properties connectionProps = new Properties();
        connectionProps.put("user", EnvVariableHandlerSingleton.getUsername());
        connectionProps.put("password", EnvVariableHandlerSingleton.getPassword());
        connectionProps.put("serverTimezone", EnvVariableHandlerSingleton.getTimeZone());
        connectionProps.put("sessionTimezone", EnvVariableHandlerSingleton.getTimeZone());
        String dbURL = EnvVariableHandlerSingleton.getDataBaseURL();
        connection = DriverManager.getConnection(dbURL, connectionProps);
        //TODO: use this lastDay var
        lastDay = YearMonth.of(this.year, this.month).atEndOfMonth().getDayOfMonth();
        List<Thread> threadList = List.of(
                new Thread(() -> this.groceries = dataBaseQueryByMonth("GROCERIES")),
                new Thread(() -> this.commute = dataBaseQueryByMonth("COMMUTE")),
                new Thread(() -> this.extra = dataBaseQueryByMonth("EXTRA")),
                new Thread(() -> this.rent = dataBaseQueryByMonth("RENT")),
                new Thread(() -> this.income = dataBaseQueryByMonth("INCOME"))
        );
        threadList.forEach(Thread::start);
        threadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    public Map<Integer, DailyStatisticRecord> getDailyStatisticRecordsByMonthByOnlyOneQuery(){
        Map<Integer, DailyStatisticRecord> dsList = new HashMap<>();
        IntStream.rangeClosed(1, lastDay).forEach(i -> {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            LocalDate localDate = LocalDate.of(year, month, i);
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            List<Integer> groceriesList = new ArrayList<>();
            int groceriesAmount = setGivenListAccordingToMapAndReturnWithSum(groceriesList, groceries, date);
            List<Integer> commuteList = new ArrayList<>();
            int commuteAmount = setGivenListAccordingToMapAndReturnWithSum(commuteList, commute, date);
            List<Integer> extraList = new ArrayList<>();
            int extraAmount = setGivenListAccordingToMapAndReturnWithSum(extraList, extra, date);
            List<Integer> rentList = new ArrayList<>();
            int rentAmount = setGivenListAccordingToMapAndReturnWithSum(rentList, rent, date);
            List<Integer> incomeList = new ArrayList<>();
            int incomeAmount = setGivenListAccordingToMapAndReturnWithSum(incomeList, income, date);
            DailyStatisticRecord dsr = new DailyStatisticRecord(
                    groceriesAmount,
                    groceriesList,
                    commuteAmount,
                    commuteList,
                    extraAmount,
                    extraList,
                    rentAmount,
                    rentList,
                    incomeAmount,
                    incomeList,
                    getBankBalanceFromDataBase(i));
            dsList.put(i, dsr);
        });
        return dsList;
    }
    private int getBankBalanceFromDataBase(int day){
        try {
            return (new BankBalanceHandler(connection, userId)).getBankBalanceByGivenDay(year, month, day);
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        return 0;
    }

    private int setGivenListAccordingToMapAndReturnWithSum(List<Integer> actualList, Map<Date, List<Integer>> actualMap, Date date){
        if(actualMap.containsKey(date)){
            actualList.addAll(actualMap.get(date));
            return actualList.stream().mapToInt(Integer::intValue).sum();
        }
        else{
            return 0;
        }
    }

    private Map<Date, List<Integer>> dataBaseQueryByMonth(String dbName){
        try{
            return (new DatabaseHandler(dbName, connection, userId)).getExpensesByGivenMonth(year, month);
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return new HashMap<>();
        }
    }
}
