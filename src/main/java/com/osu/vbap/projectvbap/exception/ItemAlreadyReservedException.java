package com.osu.vbap.projectvbap.exception;

public class ItemAlreadyReservedException extends RuntimeException{
    public ItemAlreadyReservedException(String message) {
        super(message);
    }
}
