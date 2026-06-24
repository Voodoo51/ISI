package edziekanat.isi.exceptions;

public class PaymentAlreadyPaid extends RuntimeException {
    public PaymentAlreadyPaid(String message) {
        super(message);
    }
}
