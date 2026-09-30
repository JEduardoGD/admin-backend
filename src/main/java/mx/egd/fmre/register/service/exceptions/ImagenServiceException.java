package mx.egd.fmre.register.service.exceptions;

import java.io.IOException;

import mx.egd.fmre.register.exception.FileSystemStorageServiceException;

public class ImagenServiceException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3680876940582522321L;

	public ImagenServiceException(String string, IOException e) {
		super(string, e);
	}

	public ImagenServiceException(String string) {
		super(string);
	}

	public ImagenServiceException(String string, IllegalArgumentException e) {
		super(string, e);
	}

	public ImagenServiceException(FileSystemStorageServiceException e) {
		super(e);
	}
}
