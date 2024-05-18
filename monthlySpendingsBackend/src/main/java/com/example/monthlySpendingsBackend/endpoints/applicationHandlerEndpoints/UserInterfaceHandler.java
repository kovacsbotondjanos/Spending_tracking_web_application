package com.example.monthlySpendingsBackend.endpoints.applicationHandlerEndpoints;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseReadHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseWriteAndDeleteHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.DailyStatisticRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalanceService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.Outgoing;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.OutgoingService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUserDetails;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Controller
public class UserInterfaceHandler {
    @Autowired
    private final OutgoingService outgoingService;
    @Autowired
    private final BankBalanceService bankBalanceService;
    @Autowired
    private final UserDetailService userDetailService;
    @Autowired
    private final DataBaseReadHandler dataBaseReadHandler;
    @Autowired
    private final DataBaseWriteAndDeleteHandler dataBaseWriteAndDeleteHandler;

    public UserInterfaceHandler(OutgoingService outgoingService, BankBalanceService bankBalanceService, UserDetailService userDetailService, DataBaseReadHandler dataBaseReadHandler, DataBaseWriteAndDeleteHandler dataBaseWriteAndDeleteHandler) {
        this.outgoingService = outgoingService;
        this.bankBalanceService = bankBalanceService;
        this.userDetailService = userDetailService;
        this.dataBaseReadHandler = dataBaseReadHandler;
        this.dataBaseWriteAndDeleteHandler = dataBaseWriteAndDeleteHandler;
    }


    @GetMapping(path="/login")
    public String loginHandler(){
        return "custom_login.html";
    }

    @GetMapping(path="/register")
    public String registrationHandler(){
        return "sign-up.html";
    }

    @GetMapping("/monthlyStatistics/v1")
    public RedirectView monthlyStatisticsWithoutSpecifiedMonth(RedirectAttributes attributes){
        LocalDate date = LocalDate.now();
        String year = Integer.toString(date.getYear());
        String month = Integer.toString(date.getMonthValue());
        return new RedirectView("/monthlyStatistics/v1/" + year + "/" + month);
    }

    @GetMapping("/monthlyStatistics/v1/{year}/{month}")
    public String monthlyStatisticUI(Model model, @PathVariable int year, @PathVariable int month){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {

                List<DailyStatisticRecord> databaseRead = dataBaseReadHandler.getDailyStatisticRecordsByMonth(year, month, userDetails.id());
                model.addAttribute("dataFetchedFromDb", databaseRead);
                model.addAttribute("username", userDetails.getUsername());

                LocalDate date = LocalDate.now();
                String currDay = Integer.toString(date.getDayOfMonth());
                String currMonth = Integer.toString(date.getMonthValue());
                String monthAsString = Integer.toString(month);
                int currYear = date.getYear();

                if(monthAsString.length() == 1){
                    monthAsString = "0" + monthAsString;
                }

                if(currMonth.length() == 1){
                    currMonth = "0" + currMonth;
                }

                if(currDay.length() == 1){
                    currDay = "0" + currDay;
                }

                model.addAttribute("time", year + "-" + monthAsString);
                model.addAttribute("year", currYear);
                model.addAttribute("month", currMonth);
                model.addAttribute("day", currDay);

                return "index.html";
            }
            return "error";
        }
        catch (Exception e){
            return "error";
        }
    }

    @PostMapping("/enterIntoDataBase/v1")
    public RedirectView addRecordToDataBase(@RequestParam("type") String type,
                                            @RequestParam("date") LocalDate date,
                                            @RequestParam("amount") int amount){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {

            Optional<CustomUser> userOptional = userDetailService.getUserById(userDetails.id());

            if(userOptional.isEmpty()){
                return new RedirectView("/error");
            }

            CustomUser user = userOptional.get();

            Outgoing record = new Outgoing();
            record.setUser(user);
            record.setType(type);
            record.setDate(date);
            record.setAmount(amount);

            dataBaseWriteAndDeleteHandler.dataBaseWrite(record);

            return new RedirectView("/monthlyStatistics/v1/" + date.getYear() + "/" + date.getMonthValue());
        }
        return new RedirectView("/error");
    }

    @PostMapping("/monthlyStatistics")
    public RedirectView fillInMonth(@RequestParam("month") String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate date = LocalDate.parse(dateString + "-01", formatter);
            return new RedirectView("/monthlyStatistics/v1/" + date.getYear() + "/" + date.getMonthValue());
        } catch (DateTimeParseException e) {
            System.err.println(e.getMessage());
        }
        return new RedirectView("/error");
    }

    @DeleteMapping("/deleteFromDataBase/v1/{id}")
    public RedirectView deleteRecordFromDataBase(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {

            Optional<CustomUser> userOptional = userDetailService.getUserById(userDetails.id());

            if(userOptional.isEmpty()){
                return new RedirectView("/error");
            }

            CustomUser user = userOptional.get();

            Optional<Outgoing> expense = outgoingService.getOutgoingByUserAndId(user, id);

            if(expense.isEmpty()){
                return new RedirectView("/error");
            }

            dataBaseWriteAndDeleteHandler.dataBaseDelete(expense.get());

            return new RedirectView("/monthlyStatistics/v1/" + expense.get().getDate().getYear() + "/" + expense.get().getDate().getMonthValue());
        }
        return new RedirectView("/error");
    }
}
