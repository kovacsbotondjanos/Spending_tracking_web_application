package com.example.monthlySpendingsBackend.dataBaseQueryHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseWriteAndDeleteHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.InteractionRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@CrossOrigin
@RestController
public class PostDataIntoDataBaseByJSONObject {
    private static final Logger logger = LogManager.getLogger(PostDataIntoDataBaseByJSONObject.class);
    @PostMapping("/enterIntoDataBase/v1")
    ResponseEntity<String> insertIntoDataBase(@RequestBody InteractionRecord dbWrite){
        try{
            DataBaseWriteAndDeleteHandler.InsertIntoDataBase(dbWrite);
            return new ResponseEntity<>("Successfully inserted data into the database", HttpStatus.OK);
        }
        catch(SQLException e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Couldn't enter new data into the database", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(Exception e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
