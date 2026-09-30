package mx.egd.fmre.register.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import lombok.Data;

@Data
public class MailDetaislObj implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6281625472767730608L;
	private String host;
	private int port;
	private String username;
	private String passwd;
	
	private Properties SmtpProperties;
	private List<String> toList;
	private String from;
	private List<String> cc;
	private List<String> bcc;
	private String subject;
	private byte[] emailBodyBytes;
	
	private byte[] attachedFile;
	private String attachedFileName;
}
