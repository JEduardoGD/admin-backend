package mx.egd.fmre.register.service.exceptions;

import mx.egd.fmre.register.exception.RegisterException;

public class ServiceException extends RegisterException {

    private static final long serialVersionUID = -7590885943541580487L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable e) {
        super(e);
    }

}
