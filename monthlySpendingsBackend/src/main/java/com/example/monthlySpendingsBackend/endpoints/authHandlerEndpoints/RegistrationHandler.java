package com.example.monthlySpendingsBackend.endpoints.authHandlerEndpoints;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalanceService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;

@RestController
public class RegistrationHandler {
    private final UserDetailService userService;
    private final BankBalanceService bankBalanceService;

    @Autowired
    public RegistrationHandler(UserDetailService userService, BankBalanceService bankBalanceService){
        this.userService = userService;
        this.bankBalanceService = bankBalanceService;
    }

    @PostMapping("/register")
    public RedirectView registerUser(@RequestParam("password") String password,
                                    @RequestParam("email") String email,
                                    @RequestParam("name") String name,
                                    @RequestParam("bankBalance") int bankBalance){
        try{
            LocalDate date = LocalDate.now();
            if(password == null || email == null || name == null){
                throw new IllegalArgumentException("All fields must be filled in");
            }

            CustomUser user = new CustomUser();
            user.setPassword(password);
            user.setEmail(email);
            user.setUsername(name);
            user.setRole("USER");

            userService.registerUser(user);
            bankBalanceService.registerUserWithBalance(date, user, bankBalance);
            return new RedirectView("/monthlyStatistics/v1");
        }
        catch(Exception e){
            return new RedirectView("/register?error=" + e.getMessage());
        }
    }
}
