package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BankBalanceService {
    @Autowired
    private BankBalanceRepository repository;

    //We return the bank balances of the time between the two dates, additionally we add the last record too
    public List<BankBalance> getBankBalanceByUserIdBetweenSpecificDates(Date startDate, Date endDate, Long userId){
        Optional<BankBalance> lastDayBefore = repository.findFirstByUserIdAndDateLessThanOrderByDateDesc(userId, startDate);
        List<BankBalance> balances = new ArrayList<>();
        lastDayBefore.ifPresent(balances::add);
        balances.addAll(repository.findByDateBetweenAndUserId(startDate, endDate, userId));
        return balances;
    }

    //We have to update the one record of that day and all the records after that
    public void updateBankBalance(Date date, int fluctuation, CustomUser user){
        List<BankBalance> balances = repository.findByDateAndUserIdGreaterThanEqual(date, user.getId());
        Optional<BankBalance> balanceOptional = repository.findByDateAndUserId(date, user.getId());
        balanceOptional.ifPresentOrElse(
                bankBalance -> balances.forEach(bb -> updateBankBalanceForSpecificDay(bb, fluctuation)),
                () -> insertBankBalanceForSpecificDayAndUpdateDays(date, fluctuation, user, balances)
        );
    }

    public void registerUserWithBalance(Date date, CustomUser user, int balance){
        BankBalance bb = new BankBalance();
        bb.setAmount(balance);
        bb.setUser(user);
        bb.setDate(date);
        repository.save(bb);
    }

    private void insertBankBalanceForSpecificDayAndUpdateDays(Date date, int fluctuation, CustomUser user, List<BankBalance> balancesAfter){
        BankBalance bb = new BankBalance();
        bb.setDate(date);

        Optional<BankBalance> balanceBefore = repository.findFirstByUserIdAndDateLessThanOrderByDateDesc(user.getId(), date);
        balanceBefore.ifPresentOrElse(
                balance -> bb.setAmount(balance.getAmount() + fluctuation),
                () -> bb.setAmount(fluctuation)
        );

        bb.setUser(user);
        repository.save(bb);
    }

    private void updateBankBalanceForSpecificDay(BankBalance balance, int fluctuation){
        balance.setAmount(balance.getAmount() - fluctuation);
        repository.save(balance);
    }
}
