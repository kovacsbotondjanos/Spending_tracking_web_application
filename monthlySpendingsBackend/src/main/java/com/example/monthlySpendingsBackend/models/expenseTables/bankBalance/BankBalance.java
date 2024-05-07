package com.example.monthlySpendingsBackend.models.expenseTables.bankBalance;

import com.example.monthlySpendingsBackend.models.user.CustomUser;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "BANKBALANCE")
public class BankBalance {
    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    private int amount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private CustomUser user;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }
}
