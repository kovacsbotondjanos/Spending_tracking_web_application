package com.example.monthlySpendingsBackend.dataBaseHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers.BankBalanceHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers.DatabaseHandler;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;

public class DataBaseReadHandler{
    private int year;
    private int month;
    private Map<Date, List<Integer>> groceries;
    private Map<Date, List<Integer>> commute;
    private Map<Date, List<Integer>> extra;
    private Map<Date, List<Integer>> rent;
    private Map<Date, List<Integer>> income;
    private int lastDay;

    public DataBaseReadHandler(String year, String month) throws DateTimeException, NumberFormatException{
        this.year = Integer.parseInt(year);
        this.month = Integer.parseInt(month);
        YearMonth.of(this.year, this.month);
        lastDay = YearMonth.of(this.year, this.month).atEndOfMonth().getDayOfMonth();
        List<Thread> threadList = List.of(
                new Thread(() -> {
                    this.groceries = dataBaseQueryByMonth("GROCERIES");
                }),
                new Thread(() -> {
                    this.commute = dataBaseQueryByMonth("COMMUTE");
                }),
                new Thread(() -> {
                    this.extra = dataBaseQueryByMonth("EXTRA");
                }),
                new Thread(() -> {
                    this.rent = dataBaseQueryByMonth("RENT");
                }),
                new Thread(() -> {
                    this.income = dataBaseQueryByMonth("INCOME");
                })
        );
        for(Thread t : threadList){
            t.start();
        }
        for(Thread t : threadList){
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public Map<Integer, DailyStatisticRecord> getDailyStatisticRecordsByMonthByOnlyOneQuery(){
        Map<Integer, DailyStatisticRecord> dsList = new HashMap();
        IntStream.rangeClosed(1, lastDay).forEach(i -> {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            LocalDate localDate = LocalDate.of(year, month, i);
            Date date = Date.valueOf(localDate);
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
    public int getBankBalanceFromDataBase(int day){
        try {
            return (new BankBalanceHandler()).getBankBalanceByGivenDay(year, month, day);
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
            return (new DatabaseHandler(dbName)).getExpensesByGivenMonth(year, month);
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return (new HashMap<>());
        }
    }
}
