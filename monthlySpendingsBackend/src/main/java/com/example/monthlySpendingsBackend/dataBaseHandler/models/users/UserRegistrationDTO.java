package com.example.monthlySpendingsBackend.dataBaseHandler.models.users;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    String name;
    String email;
    String password;
    int bankBalance;
}
