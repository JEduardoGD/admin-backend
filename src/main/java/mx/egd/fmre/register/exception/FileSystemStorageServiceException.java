package mx.egd.fmre.register.exception;

import mx.egd.fmre.register.service.exceptions.ServiceException;

public class FileSystemStorageServiceException extends ServiceException {

	private static final long serialVersionUID = 3476786544073378642L;

	public FileSystemStorageServiceException(String message) {
		super(message);
	}

	public FileSystemStorageServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileSystemStorageServiceException(Throwable e) {
		super(e);
	}
}