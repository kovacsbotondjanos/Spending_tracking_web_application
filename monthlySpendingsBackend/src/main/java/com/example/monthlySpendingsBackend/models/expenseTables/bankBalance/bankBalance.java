package com.example.monthlySpendingsBackend.models.expenseTables.bankBalance;

import com.example.monthlySpendingsBackend.models.user.CustomUser;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "BANKBALANCE")
public class bankBalance {
    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    private int amount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private CustomUser user;
}
