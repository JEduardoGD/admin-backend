package mx.egd.fmre.register.service.exceptions;

import lombok.Getter;

public class AddressServiceException extends ServiceException {

    private static final long serialVersionUID = 2059826105196922512L;

    @Getter
    private int httpErrorCode;

    public AddressServiceException(int httpErrorCode, String message) {
        super(message);
        this.httpErrorCode = httpErrorCode;
    }

}
