package com.sellics.model;

public class AmazonCompletionException extends RuntimeException {
    public AmazonCompletionException(String message) {
        super(message);
    }

    public AmazonCompletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
