package org.lib.subscriptionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalDurationException extends RuntimeException {
    public IllegalDurationException(String message) {
        super(message);
    }
}
