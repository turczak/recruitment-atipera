package com.atipera.recruitment_atipera.controller;

import com.atipera.recruitment_atipera.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleException(UserNotFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
                .body(Map.of(
                        "status", status.value(),
                        "message", exception.getMessage()
                ));
    }

}
