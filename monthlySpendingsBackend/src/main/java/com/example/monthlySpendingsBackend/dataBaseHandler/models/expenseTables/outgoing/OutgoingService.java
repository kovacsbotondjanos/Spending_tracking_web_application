package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalanceService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OutgoingService {
    @Autowired
    private final OutgoingRepository repository;
    private final BankBalanceService bankBalanceService;
    private final List<String> possibleTypes = List.of("GROCERIES", "EXTRA", "COMMUTE", "INCOME", "RENT");

    public OutgoingService(OutgoingRepository repository, BankBalanceService bankBalanceService) {
        this.repository = repository;
        this.bankBalanceService = bankBalanceService;
    }

    public List<Outgoing> getOutgoingExpenseByUserIdAndTypeBetweenDates(LocalDate startDate, LocalDate endDate, Long userId, String type){
        return repository.findByDateBetweenAndUserIdAndType(startDate, endDate, userId, type);
    }

    public Outgoing insertExpenseRecord(Outgoing expense) throws IllegalArgumentException {
        if(!possibleTypes.contains(expense.getType())){
            throw new IllegalArgumentException("Please provide valid data");
        }

        LocalDate date = expense.getDate();
        int amount = expense.getAmount();
        CustomUser user = expense.getUser();
        String type = expense.getType();

        if (expense.getType().equals("INCOME")) {
            bankBalanceService.updateBankBalance(date, -amount, user);
        }
        else {
            bankBalanceService.updateBankBalance(date, amount, user);
        }


        Outgoing out = new Outgoing();
        out.setDate(date);
        out.setAmount(amount);
        out.setUser(user);
        out.setType(type);
        return repository.save(out);
    }

    public void deleteExpenseRecord(Outgoing expense) throws IllegalArgumentException {
        LocalDate date = expense.getDate();
        int amount = expense.getAmount();
        CustomUser user = expense.getUser();
        String type = expense.getType();

        Optional<Outgoing> optionalExpense = repository.findByDateAndUserIdAndAmountAndType(date, user.getId(), amount, type);

        if(optionalExpense.isEmpty()){
            throw new IllegalArgumentException("Please provide a record with existing data");
        }

        if (type.equals("INCOME")) {
            bankBalanceService.updateBankBalance(date, amount, user);
        } else {
            bankBalanceService.updateBankBalance(date, -amount, user);
        }

        optionalExpense.ifPresent(repository::delete);
    }
}
