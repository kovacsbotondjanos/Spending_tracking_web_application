package com.example.monthlySpendingsBackend.endpoints.applicationHandlerEndpoints;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseReadHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.DailyStatisticRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.Map;

@Controller
public class UserInterfaceHandler {
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
    public String monthlyStatisticUI(Model model, @PathVariable String year, @PathVariable String month){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
                Map<Integer, DailyStatisticRecord> databaseRead = DataBaseReadHandler.DataBaseRead(year, month, userDetails.id());
                model.addAttribute("dataFetchedFromDb", databaseRead);
                model.addAttribute("username", userDetails.getUsername());
                return "index.html";
            }
            return "error";
        }
        catch (Exception e){
            return "error";
        }
    }
}
