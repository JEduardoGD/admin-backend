package mx.egd.fmre.register.component.exception;

import java.io.IOException;

import mx.egd.fmre.register.exception.RegisterException;

public class ComponentException extends RegisterException {

    private static final long serialVersionUID = 7832074480688336121L;

    public ComponentException(IOException e) {
        super(e);
    }

    public ComponentException(String string) {
        super(string);
    }

}
