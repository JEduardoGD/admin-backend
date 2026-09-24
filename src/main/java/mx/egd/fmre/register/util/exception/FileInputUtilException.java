package mx.egd.fmre.register.util.exception;

import java.io.IOException;
import java.net.MalformedURLException;

public class FileInputUtilException extends UtilException {

    private static final long serialVersionUID = 74182767438797277L;

    public FileInputUtilException(MimeTypesUtilException e) {
        super(e);
    }

    public FileInputUtilException(IOException e) {
        super(e);
    }

    public FileInputUtilException(String string) {
        super(string);
    }

    public FileInputUtilException(String string, IllegalArgumentException e) {
        super(string, e);
    }

    public FileInputUtilException(String string, MalformedURLException e) {
        super(string, e);
    }

}
