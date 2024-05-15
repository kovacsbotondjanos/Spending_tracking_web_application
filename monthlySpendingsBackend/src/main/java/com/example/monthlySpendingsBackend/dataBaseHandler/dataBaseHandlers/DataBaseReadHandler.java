package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.DailyStatisticRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalance;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalanceService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.Outgoing;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.OutgoingService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class DataBaseReadHandler {
    @Autowired
    private final OutgoingService outgoingService;
    @Autowired
    private final BankBalanceService bankBalanceService;

    public DataBaseReadHandler(OutgoingService outgoingService, BankBalanceService bankBalanceService) {
        this.outgoingService = outgoingService;
        this.bankBalanceService = bankBalanceService;
    }


    public Map<Integer, DailyStatisticRecord> getDailyStatisticRecordsByMonth(int year, int month, Long userId) {
        Map<Integer, DailyStatisticRecord> dsList = new HashMap<>();
        LocalDate start = LocalDate.of(year, month, 1);
        int lastDay = start.lengthOfMonth();
        LocalDate end = LocalDate.of(year, month, lastDay);

        Future<Map<LocalDate, List<Integer>>> groceriesFuture = CompletableFuture.supplyAsync(() -> dataBaseQueryByMonth("GROCERIES", start, end, userId));
        Future<Map<LocalDate, List<Integer>>> commuteFuture = CompletableFuture.supplyAsync(() -> dataBaseQueryByMonth("COMMUTE", start, end, userId));
        Future<Map<LocalDate, List<Integer>>> extraFuture = CompletableFuture.supplyAsync(() -> dataBaseQueryByMonth("EXTRA", start, end, userId));
        Future<Map<LocalDate, List<Integer>>> rentFuture = CompletableFuture.supplyAsync(() -> dataBaseQueryByMonth("RENT", start, end, userId));
        Future<Map<LocalDate, List<Integer>>> incomeFuture = CompletableFuture.supplyAsync(() -> dataBaseQueryByMonth("INCOME", start, end, userId));
        Future<List<BankBalance>> bankBalancesFuture = CompletableFuture.supplyAsync(() -> bankBalanceService.getBankBalanceByUserIdBetweenSpecificDates(start, end, userId));

        try{
            Map<LocalDate, List<Integer>> groceries = groceriesFuture.get();
            Map<LocalDate, List<Integer>> commute = commuteFuture.get();
            Map<LocalDate, List<Integer>> extra = extraFuture.get();
            Map<LocalDate, List<Integer>> rent = rentFuture.get();
            Map<LocalDate, List<Integer>> income = incomeFuture.get();
            List<BankBalance> bankBalances = bankBalancesFuture.get();
            int bankBalance;
            if(!bankBalances.isEmpty() && bankBalances.get(0).getDate().getMonthValue() < month){
                bankBalance = bankBalances.get(0).getAmount();

            }
            else{
                bankBalance = 0;
            }
            for(int i = 1; i <= lastDay; i++){
                TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                LocalDate date = LocalDate.of(year, month, i);

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
        }
        catch (InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
        return dsList;
    }

    private int setGivenListAccordingToMapAndReturnWithSum(List<Integer> actualList, Map<LocalDate, List<Integer>> actualMap, LocalDate date){
        if(actualMap.containsKey(date)){
            actualList.addAll(actualMap.get(date));
            return actualList.stream().mapToInt(Integer::intValue).sum();
        }
        else{
            return 0;
        }
    }

    private Map<LocalDate, List<Integer>> dataBaseQueryByMonth(String dbName, LocalDate start, LocalDate end, Long userId){
        Map<LocalDate, List<Integer>> dateToInt = new HashMap<>();

        List<Outgoing> expenses = outgoingService.getOutgoingExpenseByUserIdAndTypeBetweenDates(start, end, userId, dbName);

        expenses.forEach(e -> {
            LocalDate date = e.getDate();
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
