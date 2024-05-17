package com.example.monthlySpendingsBackend.dataBaseHandler.models.users;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalance;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.Outgoing;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

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
}
