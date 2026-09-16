package mx.egd.fmre.register.service;

import mx.egd.fmre.register.component.exception.IdBadgeComponentException;

public interface IdBadgeService {

    byte[] createIdBadgeService(Integer idPersona) throws IdBadgeComponentException;

}
