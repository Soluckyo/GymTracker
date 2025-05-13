package org.lib.subscriptionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalSubscriptionType extends RuntimeException {
    public IllegalSubscriptionType(String message) {
        super(message);
    }
}
