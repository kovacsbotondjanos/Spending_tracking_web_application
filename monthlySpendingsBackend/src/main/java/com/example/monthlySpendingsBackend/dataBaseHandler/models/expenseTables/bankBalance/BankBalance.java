package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "BANKBALANCE")
public class BankBalance {
    @Id
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    private int amount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private CustomUser user;
}
