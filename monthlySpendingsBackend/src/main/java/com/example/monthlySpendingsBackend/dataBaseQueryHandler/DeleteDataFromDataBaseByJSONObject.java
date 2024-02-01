package com.example.monthlySpendingsBackend.dataBaseQueryHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.DataBaseWriteHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.DataBaseWriteRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class DeleteDataFromDataBaseByJSONObject {
    @PostMapping("/deleteFromDataBase/v1")
    ResponseEntity<String> insertIntoDataBase(@RequestBody DataBaseWriteRecord dbWrite){
        try{
            (new DataBaseWriteHandler(dbWrite)).insertIntoDataBase();
            return new ResponseEntity<>("Successfully inserted data into the database", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Couldn't enter new data into the database", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
