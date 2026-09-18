package mx.egd.fmre.register.service.exceptions;

import java.io.IOException;

public class AspiranteServiceException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public AspiranteServiceException(String message) {
        super(message);
    }

	public AspiranteServiceException(IOException e) {
		super(e);
	}

}
