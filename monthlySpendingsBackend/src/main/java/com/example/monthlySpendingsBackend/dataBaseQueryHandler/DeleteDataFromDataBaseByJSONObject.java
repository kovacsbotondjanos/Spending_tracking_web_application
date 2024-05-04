package com.example.monthlySpendingsBackend.dataBaseQueryHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.InteractionRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseWriteAndDeleteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@CrossOrigin
@RestController
public class DeleteDataFromDataBaseByJSONObject {
    private static final Logger logger = LogManager.getLogger(DeleteDataFromDataBaseByJSONObject.class);
    @DeleteMapping("/deleteFromDataBase/v1")
    ResponseEntity<String> insertIntoDataBase(@RequestBody InteractionRecord dbDelete){
        try{
            DataBaseWriteAndDeleteHandler.DeleteFromDataBase(dbDelete);
            return new ResponseEntity<>("Successfully inserted data into the database", HttpStatus.OK);
        }
        catch(SQLException e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Couldn't enter new data into the database: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(Exception e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, something went wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
