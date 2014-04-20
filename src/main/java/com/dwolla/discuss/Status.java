package com.dwolla.discuss;

public enum Status {

    PROCESSED,
    PENDING,
    CANCELLED,
    FAILED,
    RECLAIMED,
    ;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
    
}
