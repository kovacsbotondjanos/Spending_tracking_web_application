package com.example.monthlySpendingsBackend.dataBaseQueryHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.DailyStatisticRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseReadHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.util.Map;

@CrossOrigin
@RestController
public class GetDataInResponseFormat {

    @GetMapping("/monthlyStatistics/v1/{year}/{month}")
    @ResponseBody()
    public ResponseEntity<?> getDailyStatistics(@PathVariable String year, @PathVariable String month) throws Exception {
        try{
            return ResponseEntity.ok(DataBaseReadHandler.DataBaseRead(year, month));
        }
        catch(DateTimeException de){
            return new ResponseEntity<>("Sorry, couldn't parse this date", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NumberFormatException ne){
            return new ResponseEntity<>("Sorry, couldn't parse this number", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(Exception ne){
            return new ResponseEntity<>("Sorry, something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
