package com.example.monthlySpendingsBackend.dataBaseQueryHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.InteractionRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseWriteAndDeleteHandler;
import com.example.monthlySpendingsBackend.models.user.CustomUser;
import com.example.monthlySpendingsBackend.models.user.CustomUserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@CrossOrigin
@RestController
public class DeleteDataFromDataBaseByJSONObject {
    private static final Logger logger = LogManager.getLogger(DeleteDataFromDataBaseByJSONObject.class);
    @DeleteMapping("/deleteFromDataBase/v1")
    ResponseEntity<String> insertIntoDataBase(@RequestBody InteractionRecord dbDelete){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
                DataBaseWriteAndDeleteHandler.DeleteFromDataBase(dbDelete, userDetails.id());
                return new ResponseEntity<>("Successfully inserted data into the database", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Unauthenticated", HttpStatus.UNAUTHORIZED);
            }
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
