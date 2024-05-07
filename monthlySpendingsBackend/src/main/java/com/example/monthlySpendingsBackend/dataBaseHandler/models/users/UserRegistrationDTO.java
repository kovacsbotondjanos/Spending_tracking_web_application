package com.example.monthlySpendingsBackend.dataBaseHandler.models.users;

public class UserRegistrationDTO {
    String name;
    String email;
    String password;
    int bankBalance;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getBankBalance() {
        return bankBalance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBankBalance(int bankBalance) {
        this.bankBalance = bankBalance;
    }
}
