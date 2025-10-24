package com.example.factory.exception;

public class InvalidExcelFormatException extends RuntimeException {
    public InvalidExcelFormatException(String message) {
        super(message);
    }
}