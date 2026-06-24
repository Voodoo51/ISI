package edziekanat.isi.exceptions;

public class PaymentProcessingErrorException extends RuntimeException {
    public PaymentProcessingErrorException(String message) {
        super(message);
    }
}
