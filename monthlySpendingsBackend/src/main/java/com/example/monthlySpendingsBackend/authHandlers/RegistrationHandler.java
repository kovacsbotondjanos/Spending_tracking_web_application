package com.example.monthlySpendingsBackend.authHandlers;

import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalanceService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.UserDetailService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.UserRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
    public ResponseEntity<?> registerUser(@ModelAttribute UserRegistrationDTO userDetails){
        try{
            LocalDate today = LocalDate.now();
            Date date = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if(userDetails.getName() == null || userDetails.getPassword() == null ||
                    userDetails.getBankBalance() < 0 || userDetails.getEmail() == null){
                throw new IllegalArgumentException("All fields must be filled in");
            }
            CustomUser user = new CustomUser();
            user.setPassword(userDetails.getPassword());
            user.setEmail(userDetails.getEmail());
            user.setUsername(userDetails.getName());
            user.setRole("USER");
            userService.registerUser(user);
            System.out.println(user.toString());
            bankBalanceService.registerUserWithBalance(date, user, userDetails.getBankBalance());
            return new ResponseEntity<>("Successfully created user", HttpStatus.CREATED);
        }
        catch(IllegalArgumentException ie){
            return new ResponseEntity<>("This username or email is already taken, " + ie.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e){
            return new ResponseEntity<>("Something went wrong while creating you account, " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
