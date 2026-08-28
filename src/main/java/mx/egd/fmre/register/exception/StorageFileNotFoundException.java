package mx.egd.fmre.register.exception;

public class StorageFileNotFoundException extends StorageException {

    private static final long serialVersionUID = 7788973559322069833L;

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}