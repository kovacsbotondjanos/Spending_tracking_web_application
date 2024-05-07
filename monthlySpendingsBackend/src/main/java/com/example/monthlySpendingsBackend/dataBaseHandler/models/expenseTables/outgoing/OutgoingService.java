package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing;

import com.example.monthlySpendingsBackend.contexts.ApplicationContextProvider;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalanceService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OutgoingService {
    @Autowired
    private OutgoingRepository repository;
    private final List<String> possibleTypes = List.of("GROCERIES", "EXTRA", "COMMUTE", "INCOME", "RENT");

    public List<Outgoing> getOutgoingExpenseByUserIdAndTypeBetweenDates(Date startDate, Date endDate, Long userId, String type){
        return repository.findByDateBetweenAndUserIdAndType(startDate, endDate, userId, type);
    }

    public Outgoing insertExpenseRecord(Date date, int amount, CustomUser user, String type) throws IllegalArgumentException {
        if(!possibleTypes.contains(type)){
            throw new IllegalArgumentException("Please provide valid data");
        }

        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        BankBalanceService bankBalanceService = context.getBean(BankBalanceService.class);

        if (type.equals("INCOME")) {
            bankBalanceService.updateBankBalance(date, amount, user);
        } else {
            bankBalanceService.updateBankBalance(date, -amount, user);
        }


        Outgoing out = new Outgoing();
        out.setDate(date);
        out.setAmount(amount);
        out.setUser(user);
        out.setType(type);
        return repository.save(out);
    }

    public void deleteExpenseRecord(Date date, int amount, CustomUser user, String type) throws IllegalArgumentException {
        Optional<Outgoing> optionalExpense = repository.findByDateAndUserIdAndAmountAndType(date, user.getId(), amount, type);
        if(optionalExpense.isEmpty()){
            throw new IllegalArgumentException("Please provide a record with existing data");
        }

        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        BankBalanceService bankBalanceService = context.getBean(BankBalanceService.class);

        if (type.equals("INCOME")) {
            bankBalanceService.updateBankBalance(date, -amount, user);
        } else {
            bankBalanceService.updateBankBalance(date, amount, user);
        }

        optionalExpense.ifPresent(repository::delete);
    }
}
