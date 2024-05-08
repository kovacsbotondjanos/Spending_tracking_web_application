package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers;

import com.example.monthlySpendingsBackend.contexts.ApplicationContextProvider;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.DailyStatisticRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalance;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalanceService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.Outgoing;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.OutgoingService;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

public class DataBaseReadHandler {
    private final int year;
    private final int month;
    private final Long userId;
    private Map<Date, List<Integer>> groceries;
    private Map<Date, List<Integer>> commute;
    private Map<Date, List<Integer>> extra;
    private Map<Date, List<Integer>> rent;
    private Map<Date, List<Integer>> income;
    private final Date start;
    private final Date end;
    List<BankBalance> bankBalances;
    private final OutgoingService outgoingService;
    private final int lastDay;

    public static Map<Integer, DailyStatisticRecord> DataBaseRead(String year, String month, Long userId) throws SQLException {
        return (new DataBaseReadHandler(year, month, userId)).getDailyStatisticRecordsByMonthByOnlyOneQuery();
    }

    private DataBaseReadHandler(String year, String month, Long userId) throws DateTimeException, NumberFormatException{
        this.year = Integer.parseInt(year);
        this.month = Integer.parseInt(month);
        this.userId = userId;
        this.lastDay = YearMonth.of(this.year, this.month).atEndOfMonth().getDayOfMonth();

        LocalDate startLoc = LocalDate.of(this.year, this.month, 1);
        this.start = Date.from(startLoc.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate endLoc = LocalDate.of(this.year, this.month, lastDay);
        this.end = Date.from(endLoc.atStartOfDay(ZoneId.systemDefault()).toInstant());

        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        //bankBalances for the month
        BankBalanceService bankBalanceService = context.getBean(BankBalanceService.class);
        this.bankBalances = bankBalanceService.getBankBalanceByUserIdBetweenSpecificDates(start, end, userId);
        //users
        this.outgoingService = context.getBean(OutgoingService.class);
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

    private Map<Integer, DailyStatisticRecord> getDailyStatisticRecordsByMonthByOnlyOneQuery(){
        Map<Integer, DailyStatisticRecord> dsList = new HashMap<>();
        int bankBalance;
        if(!bankBalances.isEmpty() && bankBalances.get(0).getDate().getMonth()+1 < month){
            bankBalance = bankBalances.get(0).getAmount();

        }
        else{
            bankBalance = 0;
        }
        for(int i = 1; i <= lastDay; i++){
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            LocalDate localDate = LocalDate.of(year, month, i);
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Optional<BankBalance> balanceOptional = bankBalances.stream().filter(bb -> date.equals(bb.getDate())).findFirst();
            if(balanceOptional.isPresent()){
                bankBalance = balanceOptional.get().getAmount();
            }

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
                    bankBalance);
            dsList.put(i, dsr);
        }
        return dsList;
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
        Map<Date, List<Integer>> dateToInt = new HashMap<>();


        List<Outgoing> expenses = outgoingService.getOutgoingExpenseByUserIdAndTypeBetweenDates(start, end, userId, dbName);

        expenses.forEach(e -> {
            Date date = e.getDate();
            int amount = e.getAmount();
            if(dateToInt.containsKey(date)){
                dateToInt.get(date).add(amount);
            }
            else{
                List<Integer> initList = new ArrayList<>();
                initList.add(amount);
                dateToInt.put(date, initList);
            }
        });
        return dateToInt;
    }
}
