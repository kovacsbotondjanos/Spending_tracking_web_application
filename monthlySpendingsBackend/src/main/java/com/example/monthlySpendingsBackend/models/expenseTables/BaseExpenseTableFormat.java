package com.example.monthlySpendingsBackend.models.expenseTables;

import com.example.monthlySpendingsBackend.models.user.CustomUser;
import jakarta.persistence.*;

import java.util.Date;

@MappedSuperclass
public abstract class BaseExpenseTableFormat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private int amount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private CustomUser user;
}
