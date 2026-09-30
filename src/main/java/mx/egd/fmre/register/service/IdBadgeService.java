package mx.egd.fmre.register.service;

import mx.egd.fmre.register.service.exceptions.IdBadgeServiceException;

public interface IdBadgeService {

    byte[] createIdBadgeService(Integer idPersona) throws IdBadgeServiceException;

    boolean sendIdBadge(Integer idPersona) throws IdBadgeServiceException;

	String createIdBadgdFileName(Integer idPersona) throws IdBadgeServiceException;


}
