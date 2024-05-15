package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BankBalanceRepository extends JpaRepository<BankBalance, Long> {
    List<BankBalance> findByDateAndUserIdGreaterThanEqual(LocalDate date, Long userId);
    Optional<BankBalance> findByDateAndUserId(LocalDate date, Long userId);
    List<BankBalance> findByDateBetweenAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
    Optional<BankBalance> findFirstByUserIdAndDateLessThanOrderByDateDesc(Long userId, LocalDate date);
}
