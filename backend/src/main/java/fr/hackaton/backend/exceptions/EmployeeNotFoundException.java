package fr.hackaton.backend.exceptions;

public class EmployeeNotFoundException extends RuntimeException{
    
    public EmployeeNotFoundException(String message){
       super(message);
     }
}
