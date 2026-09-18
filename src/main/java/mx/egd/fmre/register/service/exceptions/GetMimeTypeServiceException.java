package mx.egd.fmre.register.service.exceptions;

import java.io.IOException;

import mx.egd.fmre.register.exception.FileSystemStorageServiceException;

public class GetMimeTypeServiceException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -179550029606426466L;

	public GetMimeTypeServiceException(FileSystemStorageServiceException e) {
		super(e);
	}

	public GetMimeTypeServiceException(IOException e) {
		super(e);
	}
}
