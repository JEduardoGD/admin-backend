package mx.egd.fmre.register.util.exception;

import mx.egd.fmre.register.exception.RegisterException;

public class UtilException extends RegisterException {

    private static final long serialVersionUID = -1801883026799480625L;

    public UtilException(String message) {
        super(message);
    }

    public UtilException(Throwable e) {
        super(e);
    }

}
