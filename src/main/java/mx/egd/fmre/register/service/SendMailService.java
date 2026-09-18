package mx.egd.fmre.register.service;

import mx.egd.fmre.register.dto.MailDetaislObj;
import mx.egd.fmre.register.service.exceptions.SendMailServiceException;

public interface SendMailService {

	boolean sendHtml(MailDetaislObj mailDetailsObj) throws SendMailServiceException;

}
