package mx.egd.fmre.register.util.exception;

import java.io.IOException;

import mx.egd.fmre.register.exception.RegisterException;

public abstract class UtilException extends RegisterException {

    private static final long serialVersionUID = -1801883026799480625L;

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String string, Throwable e) {
        super(string, e);
    }

    public UtilException(IOException e) {
        super(e);
    }

    public UtilException(MimeTypesUtilException e) {
        super(e);
    }

}
