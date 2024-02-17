package com.example.monthlySpendingsBackend.dataBaseQueryHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.InteractionRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseWriteAndDeleteHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class DeleteDataFromDataBaseByJSONObject {
    @DeleteMapping("/deleteFromDataBase/v1")
    ResponseEntity<String> insertIntoDataBase(@RequestBody InteractionRecord dbDelete){
        try{
            DataBaseWriteAndDeleteHandler.DeleteFromDataBase(dbDelete);
            return new ResponseEntity<>("Successfully inserted data into the database", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Couldn't enter new data into the database", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
