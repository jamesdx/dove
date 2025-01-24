package com.helix.dove.exception;

/**
 * Exception thrown when there is an error collecting metrics from a service instance.
 */
public class MetricCollectionException extends RuntimeException {
    /**
     * Constructs a new metric collection exception with the specified detail message.
     *
     * @param message the detail message
     */
    public MetricCollectionException(String message) {
        super(message);
    }

    /**
     * Constructs a new metric collection exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public MetricCollectionException(String message, Throwable cause) {
        super(message, cause);
    }
} 