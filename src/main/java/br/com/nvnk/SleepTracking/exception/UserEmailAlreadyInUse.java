package br.com.nvnk.SleepTracking.exception;

public class UserEmailAlreadyInUse extends RuntimeException {
    public UserEmailAlreadyInUse(String message) {
        super(message);
    }
}
