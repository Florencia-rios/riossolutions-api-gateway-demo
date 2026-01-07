package dev.riossolutions.api_gateway.exception;

public class BadRequestCustomException extends RuntimeException{
    public BadRequestCustomException (String message){
        super(message);
    }
}