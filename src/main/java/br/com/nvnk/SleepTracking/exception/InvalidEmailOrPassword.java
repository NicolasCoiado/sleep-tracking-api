package br.com.nvnk.SleepTracking.exception;

public class InvalidEmailOrPassword extends RuntimeException {
    public InvalidEmailOrPassword(String message) {
        super(message);
    }
}
