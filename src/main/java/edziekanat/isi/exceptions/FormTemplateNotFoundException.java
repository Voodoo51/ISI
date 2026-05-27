package edziekanat.isi.exceptions;

public class FormTemplateNotFoundException extends RuntimeException {
    public FormTemplateNotFoundException(String id) {
        super("Form with id " + id + " not found.");
    }
}
