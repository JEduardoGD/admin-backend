package mx.egd.fmre.register.util.exception;

import java.io.IOException;

public class MimeTypesUtilException extends UtilException {

    private static final long serialVersionUID = 4394862108488670897L;

    public MimeTypesUtilException(String message) {
        super(message);
    }

    public MimeTypesUtilException(IOException e) {
        super(e);
    }

}
