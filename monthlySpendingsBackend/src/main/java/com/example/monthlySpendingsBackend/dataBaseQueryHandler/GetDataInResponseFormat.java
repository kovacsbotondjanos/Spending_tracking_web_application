package com.example.monthlySpendingsBackend.dataBaseQueryHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.DailyStatisticRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.DataBaseReadHandler;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.util.Map;

@CrossOrigin
@RestController
public class GetDataInResponseFormat {

    @GetMapping("/monthlyStatistics/v1/{year}/{month}")
    @ResponseBody()
    public Map<Integer, DailyStatisticRecord> getDailyStatistics(@PathVariable String year, @PathVariable String month) throws Exception {
        try{
            return (new DataBaseReadHandler(year, month)).getDailyStatisticRecordsByMonthByOnlyOneQuery();
        }
        //TODO: exception handling
        catch(DateTimeException de){
            throw new Exception();
        }catch(NumberFormatException ne){
            throw new Exception();
        }
    }
}
