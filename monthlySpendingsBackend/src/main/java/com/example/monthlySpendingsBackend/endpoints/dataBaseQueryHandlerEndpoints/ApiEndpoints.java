package com.example.monthlySpendingsBackend.endpoints.dataBaseQueryHandlerEndpoints;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseReadHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseWriteAndDeleteHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.Outgoing;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUserDetails;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.UserDetailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Optional;

@CrossOrigin
@RestController
public class ApiEndpoints {
    @Autowired
    private final DataBaseReadHandler dataBaseReadHandler;
    @Autowired
    private final DataBaseWriteAndDeleteHandler dataBaseWriteAndDeleteHandler;
    @Autowired
    private final UserDetailService userDetailService;
    private static final Logger logger = LogManager.getLogger(ApiEndpoints.class);

    public ApiEndpoints(DataBaseReadHandler dataBaseReadHandler, DataBaseWriteAndDeleteHandler dataBaseWriteAndDeleteHandler, UserDetailService userDetailService) {
        this.dataBaseReadHandler = dataBaseReadHandler;
        this.dataBaseWriteAndDeleteHandler = dataBaseWriteAndDeleteHandler;
        this.userDetailService = userDetailService;
    }

    @DeleteMapping("/api/deleteFromDataBase/v1")
    ResponseEntity<String> deleteFromDataBase(@RequestParam("type") String type,
                                              @RequestParam("date") LocalDate date,
                                              @RequestParam("amount") int amount){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
                Optional<CustomUser> user = userDetailService.getUserById(userDetails.id());

                if(user.isEmpty()){
                    return new ResponseEntity<>("Unauthenticated", HttpStatus.UNAUTHORIZED);
                }

                Outgoing expense = new Outgoing();
                expense.setAmount(amount);
                expense.setType(type);
                expense.setDate(date);
                expense.setUser(user.get());
                dataBaseWriteAndDeleteHandler.dataBaseDelete(expense);

                return new ResponseEntity<>("Successfully inserted data into the database", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Unauthenticated", HttpStatus.UNAUTHORIZED);
            }
        }
        catch(Exception e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, something went wrong: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/api/monthlyStatistics/v1/{year}/{month}")
    @ResponseBody()
    public ResponseEntity<?> getDailyStatistics(@PathVariable int year, @PathVariable int month){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
                return ResponseEntity.ok(dataBaseReadHandler.getDailyStatisticRecordsByMonth(year, month, userDetails.id()));
            }
            else{
                return new ResponseEntity<>("Unauthenticated", HttpStatus.UNAUTHORIZED);
            }
        }
        catch(DateTimeException e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, couldn't parse this date: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
        catch(NumberFormatException e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, couldn't parse this number: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
        catch(Exception e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, something went wrong: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("/api/enterIntoDataBase/v1")
    ResponseEntity<String> insertIntoDataBase(@RequestParam("type") String type,
                                              @RequestParam("date") LocalDate date,
                                              @RequestParam("amount") int amount){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
                Optional<CustomUser> user = userDetailService.getUserById(userDetails.id());

                if(user.isEmpty()){
                    return new ResponseEntity<>("Unauthenticated", HttpStatus.UNAUTHORIZED);
                }

                Outgoing expense = new Outgoing();
                expense.setAmount(amount);
                expense.setType(type);
                expense.setDate(date);
                expense.setUser(user.get());
                dataBaseWriteAndDeleteHandler.dataBaseWrite(expense);

                return new ResponseEntity<>("Successfully inserted data into the database", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Unauthenticated", HttpStatus.UNAUTHORIZED);
            }
        }
        catch(Exception e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, something went wrong: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
