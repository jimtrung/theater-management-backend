package com.github.jimtrung.theater.exception.user;

public class MismatchedAuthProviderException extends RuntimeException {
    public MismatchedAuthProviderException(String mesasge) {
        super(mesasge);
    }
}
