package com.example.springshopbe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ManufacturerException extends RuntimeException {
    public ManufacturerException(String message) {
        super(message);
    }
}
