package org.lib.subscriptionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StatusAlreadySetException extends RuntimeException {
    public StatusAlreadySetException(String message) {
        super(message);
    }
}
