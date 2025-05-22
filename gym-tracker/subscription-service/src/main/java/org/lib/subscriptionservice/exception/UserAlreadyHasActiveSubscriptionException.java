package org.lib.subscriptionservice.exception;

public class UserAlreadyHasActiveSubscriptionException extends RuntimeException {
    public UserAlreadyHasActiveSubscriptionException(String message) {
        super(message);
    }
}
