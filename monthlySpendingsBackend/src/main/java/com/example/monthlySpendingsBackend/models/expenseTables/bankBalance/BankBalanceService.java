package com.example.monthlySpendingsBackend.models.expenseTables.bankBalance;

import com.example.monthlySpendingsBackend.models.expenseTables.outgoing.Outgoing;
import com.example.monthlySpendingsBackend.models.expenseTables.outgoing.OutgoingRepository;
import com.example.monthlySpendingsBackend.models.user.CustomUser;
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

    public List<BankBalance> getBankBalanceExpenseByUserIdAndTypeBetweenDates(Date startDate, Date endDate, Long userId, String type){
        return repository.findByDateBetweenAndUserIdAndType(startDate, endDate, userId, type);
    }

    private Optional<BankBalance> getBankBalanceForSpecificDate(Date date){
        return repository.findByDate(date);
    }

    public void updateBankBalance(Date date, int fluctuation, CustomUser user){
        //TODO: get the whole list of bank balances after the first day!
        Optional<BankBalance> balance = getBankBalanceForSpecificDate(date);
        balance.ifPresentOrElse(
                b -> updateBankBalanceForSpecificDay(b, fluctuation),
                () -> insertBankBalanceForSpecificDay(date, fluctuation, user)
        );
    }

    private BankBalance insertBankBalanceForSpecificDay(Date date, int fluctuation, CustomUser user){
        BankBalance bb = new BankBalance();
        bb.setDate(date);
        //TODO: find the amount
        bb.setUser(user);
        return repository.save(bb);
    }

    private BankBalance updateBankBalanceForSpecificDay(BankBalance balance, int fluctuation){
        BankBalance bb = new BankBalance();
        bb.setAmount(bb.getAmount() - fluctuation);
        return repository.save(bb);
    }
}
