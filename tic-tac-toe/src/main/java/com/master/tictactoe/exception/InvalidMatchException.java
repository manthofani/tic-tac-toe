package com.master.tictactoe.exception;

public class InvalidMatchException extends Exception{

    private String message;

    public InvalidMatchException(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
