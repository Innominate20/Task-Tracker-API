package dev.innom.Task_Tracker_API.exception;

public class UserTypeMismatchException extends RuntimeException{

    public UserTypeMismatchException(String message){
        super(message);
    }
}
