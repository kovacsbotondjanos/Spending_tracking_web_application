package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OutgoingRepository extends JpaRepository<Outgoing, Long> {
    List<Outgoing> findByUserId(Long userId);
    Optional<Outgoing> findByDateAndUserIdAndAmountAndType(Date date, Long userId, int amount, String type);
    List<Outgoing> findByDateBetweenAndUserIdAndType(Date startDate, Date endDate, Long userId, String type);
}
