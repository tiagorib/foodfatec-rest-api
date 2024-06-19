package br.com.fatecrestapi.FoodFatec.exception;

public class FindUserServiceException extends RuntimeException {
    public FindUserServiceException(String message) {
        super(message);
    }
}
