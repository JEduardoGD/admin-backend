package mx.egd.fmre.register.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendIdbadgeResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5719448982164523898L;
	private boolean error;
	String errorText;
}
