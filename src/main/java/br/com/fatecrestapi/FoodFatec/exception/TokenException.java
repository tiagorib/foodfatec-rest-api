package br.com.fatecrestapi.FoodFatec.exception;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}