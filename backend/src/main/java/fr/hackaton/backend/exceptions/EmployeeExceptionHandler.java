package fr.hackaton.backend.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class EmployeeExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(EmployeeNotFoundException ex, WebRequest request) {
        ErrorMessage error = new ErrorMessage(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                "User Not Found",
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SkillNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleSkillNotFoundException(SkillNotFoundException ex, WebRequest request) {
        ErrorMessage error = new ErrorMessage(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                "Skill Not Found",
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

