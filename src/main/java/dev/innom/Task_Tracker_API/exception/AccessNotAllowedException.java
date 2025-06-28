package dev.innom.Task_Tracker_API.exception;

import jakarta.persistence.Access;

public class AccessNotAllowedException extends RuntimeException{

    public AccessNotAllowedException(String message){
        super(message);
    }
}
