package mx.egd.fmre.register.service.exceptions;

import jakarta.mail.MessagingException;

public class SendMailServiceException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4632396523981664763L;

	public SendMailServiceException(MessagingException e) {
		super(e);
	}

}
