package com.example.monthlySpendingsBackend.dataBaseQueryHandlerEndpoints;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers.DataBaseWriteAndDeleteHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.InteractionRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
                DataBaseWriteAndDeleteHandler.InsertIntoDataBase(dbWrite, userDetails.id());
                return new ResponseEntity<>("Successfully inserted data into the database", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Unauthenticated", HttpStatus.UNAUTHORIZED);
            }
        }
        catch(Exception e){
            logger.debug(e.getMessage());
            return new ResponseEntity<>("Sorry, something went wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
