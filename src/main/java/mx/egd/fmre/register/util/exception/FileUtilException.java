package mx.egd.fmre.register.util.exception;

import java.io.IOException;

public class FileUtilException extends UtilException {

    private static final long serialVersionUID = -529040598889509466L;

    public FileUtilException(IOException e) {
        super(e);
    }

}
