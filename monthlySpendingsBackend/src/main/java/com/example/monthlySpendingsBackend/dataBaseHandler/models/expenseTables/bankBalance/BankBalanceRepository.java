package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BankBalanceRepository extends JpaRepository<BankBalance, Long> {
    List<BankBalance> findByUserId(Long userId);
    //TODO: when date is bigger then current one, find it, not like we do rn
    List<BankBalance> findByDateGreaterThan(Date date);
    Optional<BankBalance> findByDate(Date date);
    List<BankBalance> findByDateBetweenAndUserId(Date startDate, Date endDate, Long userId);
}
