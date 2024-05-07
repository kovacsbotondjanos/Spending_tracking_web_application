package com.example.monthlySpendingsBackend.models.expenseTables.outgoing;

import com.example.monthlySpendingsBackend.models.user.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OutgoingService {
    @Autowired
    private OutgoingRepository repository;

    public List<Outgoing> getExpenseForUserId(Long userId){
        return repository.findByUserId(userId);
    }

    public List<Outgoing> getOutgoingExpenseByUserIdAndTypeBetweenDates(Date startDate, Date endDate, Long userId, String type){
        return repository.findByDateBetweenAndUserIdAndType(startDate, endDate, userId, type);
    }

    public Outgoing insertExpenseRecord(Date date, int amount, CustomUser user, String type){
        Outgoing out = new Outgoing();
        out.setDate(date);
        out.setAmount(amount);
        out.setUser(user);
        out.setType(type);
        return repository.save(out);
    }
}
