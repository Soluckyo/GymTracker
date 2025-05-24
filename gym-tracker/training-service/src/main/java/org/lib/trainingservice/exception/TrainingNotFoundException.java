package org.lib.trainingservice.exception;

public class TrainingNotFoundException extends RuntimeException {
    public TrainingNotFoundException(String message) {
        super(message);
    }
}
