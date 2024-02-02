package com.example.monthlySpendingsBackend.dataBaseQueryHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseWriteAndDeleteHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.InteractionRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class PostDataIntoDataBaseByJSONObject {
    @PostMapping("/enterIntoDataBase/v1")
    ResponseEntity<String> insertIntoDataBase(@RequestBody InteractionRecord dbWrite){
        try{
            DataBaseWriteAndDeleteHandler.InsertIntoDataBase(dbWrite);
            return new ResponseEntity<>("Successfully inserted data into the database", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Couldn't enter new data into the database", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
