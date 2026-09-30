package mx.egd.fmre.register.exception;

import mx.egd.fmre.register.service.exceptions.ServiceException;

public class UnsupportedImageTypeException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public UnsupportedImageTypeException(String message) {
        super(message);
    }

    public UnsupportedImageTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
