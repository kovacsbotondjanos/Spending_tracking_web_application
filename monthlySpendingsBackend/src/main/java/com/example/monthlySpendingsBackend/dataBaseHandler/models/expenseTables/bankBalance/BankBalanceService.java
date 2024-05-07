package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BankBalanceService {
    @Autowired
    private BankBalanceRepository repository;

    public List<BankBalance> getBankBalanceForUserId(Long userId){
        return repository.findByUserId(userId);
    }

    public List<BankBalance> getBankBalanceExpenseByUserIdAndTypeBetweenDates(Date startDate, Date endDate, Long userId){
        return repository.findByDateBetweenAndUserId(startDate, endDate, userId);
    }

    private Optional<BankBalance> getBankBalanceForSpecificDate(Date date){
        return repository.findByDate(date);
    }

    public void updateBankBalance(Date date, int fluctuation){
        //TODO: get the whole list of bank balances after the first day!
        Optional<BankBalance> balance = getBankBalanceForSpecificDate(date);
        List<BankBalance> balancesAfter = bankBalancesAfterCertainDate(date);
    }

    private List<BankBalance> bankBalancesAfterCertainDate(Date date){
        return repository.findByDateGreaterThan(date);
    }

    private BankBalance insertBankBalanceForSpecificDay(Date date, int fluctuation, CustomUser user, List<BankBalance> balancesAfter){
        BankBalance bb = new BankBalance();
        bb.setDate(date);
        //TODO: find the amount
        //TODO: refresh all the balances in the list
        bb.setUser(user);
        return repository.save(bb);
    }

    private BankBalance updateBankBalanceForSpecificDay(BankBalance balance, int fluctuation, List<BankBalance> balancesAfter){
        //TODO: refresh all the balances in the list
        BankBalance bb = new BankBalance();
        bb.setAmount(bb.getAmount() - fluctuation);
        return repository.save(bb);
    }
}
