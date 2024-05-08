package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BankBalanceRepository extends JpaRepository<BankBalance, Long> {
    List<BankBalance> findByDateAndUserIdGreaterThanEqual(Date date, Long userId);
    Optional<BankBalance> findByDateAndUserId(Date date, Long userId);
    List<BankBalance> findByDateBetweenAndUserId(Date startDate, Date endDate, Long userId);
    Optional<BankBalance> findFirstByUserIdAndDateLessThanOrderByDateDesc(Long userId, Date date);
}
