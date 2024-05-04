package com.example.monthlySpendingsBackend.dataBaseQueryHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseReadHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.DateTimeException;

@CrossOrigin
@RestController
public class GetDataInResponseFormat {
    private static final Logger logger = LogManager.getLogger(GetDataInResponseFormat.class);
    @GetMapping("/monthlyStatistics/v1/{year}/{month}")
    @ResponseBody()
    public ResponseEntity<?> getDailyStatistics(@PathVariable String year, @PathVariable String month){
        try{
            return ResponseEntity.ok(DataBaseReadHandler.DataBaseRead(year, month));
        }
        catch(DateTimeException e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, couldn't parse this date: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(SQLException e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, something went wrong while connecting to the database: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NumberFormatException e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, couldn't parse this number: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(Exception e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, something went wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
