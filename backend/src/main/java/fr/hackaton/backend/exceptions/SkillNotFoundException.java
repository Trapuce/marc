package fr.hackaton.backend.exceptions;



public class SkillNotFoundException extends RuntimeException {
    
    public SkillNotFoundException(String message){

        super(message) ;
    }
}

