package com.example.monthlySpendingsBackend.dataBaseHandler.models.users;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalance;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.Outgoing;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "USER")
@ToString(exclude = {"outgoings", "bankBalances"})
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BankBalance> bankBalances;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Outgoing> outgoings;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
