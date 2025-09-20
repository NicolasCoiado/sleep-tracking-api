package br.com.nvnk.SleepTracking.exception;

public class SleepAttemptNotFoundException extends RuntimeException {
    public SleepAttemptNotFoundException(String message) {
        super(message);
    }
}
