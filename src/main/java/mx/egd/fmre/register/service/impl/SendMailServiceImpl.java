package mx.egd.fmre.register.service.impl;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.dto.MailDetaislObj;
import mx.egd.fmre.register.service.FileSystemStorageService;
import mx.egd.fmre.register.service.SendMailService;
import mx.egd.fmre.register.service.exceptions.SendMailServiceException;
import mx.egd.fmre.register.util.FileInputUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMailServiceImpl extends FileInputUtil implements SendMailService {

	@Override
	public boolean sendHtml(MailDetaislObj mailDetailsObj) throws SendMailServiceException {
		MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			messageHelper.setFrom(new InternetAddress(mailDetailsObj.getFrom()));
			messageHelper.setReplyTo(new InternetAddress(mailDetailsObj.getFrom()));
			messageHelper.setTo(toInternetAddress(mailDetailsObj.getToList()));
			InternetAddress[] bccArray = toInternetAddress(mailDetailsObj.getBcc());
			if (bccArray != null && bccArray.length > 0) {
				messageHelper.setBcc(bccArray);
			}
			messageHelper.setSubject(mailDetailsObj.getSubject());
			String content = new String(mailDetailsObj.getEmailBodyBytes(), "UTF-8");
			messageHelper.setText(content, true);

			String mimeType = "application/octet-stream";
			try (java.io.InputStream inputStream = new ByteArrayInputStream(mailDetailsObj.getAttachedFile())) {
				mimeType = getMimeType(inputStream);
			}

			ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(mailDetailsObj.getAttachedFile(), mimeType);

			messageHelper.addAttachment(mailDetailsObj.getAttachedFileName(), byteArrayDataSource);
		};

		JavaMailSenderImpl javaMailSender = getJavaMailSender(mailDetailsObj);
		javaMailSender.send(mimeMessagePreparator);
		return true;
	}
	
	private JavaMailSenderImpl getJavaMailSender(MailDetaislObj mailDetailsObj) {
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setHost(mailDetailsObj.getHost());
		javaMailSenderImpl.setPort(mailDetailsObj.getPort());
		javaMailSenderImpl.setUsername(mailDetailsObj.getUsername());
		javaMailSenderImpl.setPassword(mailDetailsObj.getPasswd());
		Properties javaMailProperties = javaMailSenderImpl.getJavaMailProperties();
		for(Object keyObj : mailDetailsObj.getSmtpProperties().keySet()) {
			Object valObj = mailDetailsObj.getSmtpProperties().get(keyObj);
			String key = keyObj != null && keyObj instanceof String ? (String) keyObj : null;
			String val = valObj != null && valObj instanceof String ? (String) valObj : null;
			javaMailProperties.put(key, val);
		}
		return javaMailSenderImpl;
	}

	private InternetAddress[] toInternetAddress(List<String> addressString) {
		List<InternetAddress> addressList = addressString.stream().map(a -> {
			InternetAddress internetAddress = null;
			try {
				internetAddress = new InternetAddress(a);
			} catch (AddressException e) {
				log.error(e.getMessage());
			}
			return internetAddress;
		}).toList();
		InternetAddress[] internetAddressArray = new InternetAddress[addressList.size()];
		addressList.toArray(internetAddressArray); // fill the array
		return internetAddressArray;
	}
}
