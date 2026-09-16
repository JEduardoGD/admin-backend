package mx.egd.fmre.register.service;

import mx.egd.fmre.register.service.exceptions.CredencialControllerSeviceException;

public interface IdBadgeService {

    byte[] createIdBadgeService(Integer idPersona) throws CredencialControllerSeviceException;

}
