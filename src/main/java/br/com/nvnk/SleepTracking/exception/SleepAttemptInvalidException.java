package br.com.nvnk.SleepTracking.exception;

public class SleepAttemptInvalidException extends RuntimeException {
    public SleepAttemptInvalidException(String message) {
        super(message);
    }
}
