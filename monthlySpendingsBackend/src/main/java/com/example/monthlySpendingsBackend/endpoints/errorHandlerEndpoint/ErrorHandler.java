package com.example.monthlySpendingsBackend.endpoints.errorHandlerEndpoint;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class ErrorHandler {
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<String> handle404Errors() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry, this link does not exist");
    }

    @RequestMapping(value = "/**", method = {   GET,
                                                HEAD,
                                                POST,
                                                PUT,
                                                PATCH,
                                                DELETE,
                                                OPTIONS,
                                                TRACE
                                            })
    public ResponseEntity<String> handle405Errors() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Sorry, this link does not exist");
    }
}