package mx.egd.fmre.register.service.exceptions;

import mx.egd.fmre.register.component.exception.IdBadgeComponentException;

public class CredencialControllerSeviceException extends ServiceException {

    private static final long serialVersionUID = 4958672987245843902L;

    public CredencialControllerSeviceException(IdBadgeComponentException e) {
        super(e);
    }

}
