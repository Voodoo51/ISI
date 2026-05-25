package edziekanat.isi.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("User unauthorized.");
    }
}
