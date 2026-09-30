package mx.egd.fmre.register.service.exceptions;

import java.io.IOException;

import mx.egd.fmre.register.component.exception.IdBadgeComponentException;

public class IdBadgeServiceException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8250838325529397325L;

	public IdBadgeServiceException(String string) {
		super(string);
	}

	public IdBadgeServiceException(IdBadgeComponentException e) {
		super(e);
	}

	public IdBadgeServiceException(IOException e) {
		super(e);
	}

	public IdBadgeServiceException(SendMailServiceException e) {
		super(e);
	}

}
