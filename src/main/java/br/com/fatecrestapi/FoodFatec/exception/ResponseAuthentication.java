package br.com.fatecrestapi.FoodFatec.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;


public class ResponseAuthentication<T> {
    @ExceptionHandler({EntityNotFoundException.class, NoHandlerFoundException.class})
    public static <T> Object response(T object, T statusCode) {
        return new HashMap<String, T>() {
            private static final long serialVersionUID = 1L;
            {
                put("result", object);
                put("status", statusCode);
            }
        };
    }
}