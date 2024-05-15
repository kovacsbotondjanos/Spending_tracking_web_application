package com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "OUTGOING_EXPENSES")
public class Outgoing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int amount;
    private String type;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private CustomUser user;
}
