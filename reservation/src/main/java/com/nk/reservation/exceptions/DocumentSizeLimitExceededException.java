package com.nk.reservation.exceptions;

public class DocumentSizeLimitExceededException extends RuntimeException {
    public DocumentSizeLimitExceededException(String message) {
        super(message);
    }
}
