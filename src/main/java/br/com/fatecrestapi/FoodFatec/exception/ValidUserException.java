package br.com.fatecrestapi.FoodFatec.exception;

public class ValidUserException extends RuntimeException {
    public ValidUserException(String message) {
        super(message);
    }
}