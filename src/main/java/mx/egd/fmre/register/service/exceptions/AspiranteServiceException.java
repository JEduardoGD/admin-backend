package mx.egd.fmre.register.service.exceptions;

import java.io.IOException;

import mx.egd.fmre.register.util.exception.UtilException;

public class AspiranteServiceException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public AspiranteServiceException(String message) {
        super(message);
    }

	public AspiranteServiceException(IOException e) {
		super(e);
	}

    public AspiranteServiceException(UtilException e) {
        super(e);
    }

}
