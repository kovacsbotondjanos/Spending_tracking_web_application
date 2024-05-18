package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OutgoingRepository extends JpaRepository<Outgoing, Long> {
    List<Outgoing> findByUserId(Long userId);
    Optional<Outgoing> findByUserAndId(CustomUser user, Long id);
    Optional<Outgoing> findByDateAndUserIdAndAmountAndType(LocalDate date, Long userId, int amount, String type);
    List<Outgoing> findByDateBetweenAndUserIdAndType(LocalDate startDate, LocalDate endDate, Long userId, String type);
}
