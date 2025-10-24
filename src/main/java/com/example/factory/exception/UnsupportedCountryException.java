package com.example.factory.exception;

public class UnsupportedCountryException extends RuntimeException {
    public UnsupportedCountryException(String message) {
        super(message);
    }
}