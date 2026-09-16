package mx.egd.fmre.register.component.exception;

import java.io.IOException;

public class IdBadgeComponentException extends ComponentException {

    private static final long serialVersionUID = -3505538629722684803L;

    public IdBadgeComponentException(IOException e) {
        super(e);
    }

    public IdBadgeComponentException(String string) {
        super(string);
    }

}
