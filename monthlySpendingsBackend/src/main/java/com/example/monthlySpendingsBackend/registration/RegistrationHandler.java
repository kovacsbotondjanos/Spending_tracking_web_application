package com.example.monthlySpendingsBackend.registration;

import com.example.monthlySpendingsBackend.models.user.CustomUser;
import com.example.monthlySpendingsBackend.models.user.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationHandler {
    private final UserDetailService userService;

    @Autowired
    public RegistrationHandler(UserDetailService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CustomUser user){
        try{
            userService.registerUser(user);
            return new ResponseEntity<>("Successfully created user", HttpStatus.CREATED);
        }
        catch(IllegalArgumentException ie){
            return new ResponseEntity<>("This username or email is already taken, " + ie.getMessage(), HttpStatus.CREATED);
        }
    }
}
