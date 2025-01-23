package com.helix.dove.exception;

import lombok.Getter;

@Getter
public class RouteException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;
    private final Object[] args;

    public RouteException(String errorCode, String errorMessage, Object... args) {
        super(String.format(errorMessage, args));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.args = args;
    }

    public static class ErrorCodes {
        public static final String ROUTE_NOT_FOUND = "ROUTE_001";
        public static final String INVALID_ROUTE = "ROUTE_002";
        public static final String INVALID_PREDICATE = "ROUTE_003";
        public static final String INVALID_FILTER = "ROUTE_004";
        public static final String DUPLICATE_ROUTE = "ROUTE_005";
        public static final String ROUTE_ALREADY_EXISTS = "ROUTE_006";
        public static final String ROUTE_DISABLED = "ROUTE_007";
    }
} 