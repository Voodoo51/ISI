package edziekanat.isi.exceptions;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException() {
        super("Wrong credentials.");
    }
}
