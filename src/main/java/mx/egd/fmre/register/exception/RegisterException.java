package mx.egd.fmre.register.exception;

public class RegisterException extends Exception {

    private static final long serialVersionUID = -958234470745003510L;

    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterException(Throwable e) {
        super(e);
    }

}
