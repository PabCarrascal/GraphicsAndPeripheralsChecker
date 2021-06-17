package com.kayenterprise.exceptions;

public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(String message) {
        super("Image '"+message+"' not found");
    }
}
