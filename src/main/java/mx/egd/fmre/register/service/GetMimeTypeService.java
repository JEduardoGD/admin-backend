package mx.egd.fmre.register.service;

import mx.egd.fmre.register.service.exceptions.GetMimeTypeServiceException;

public interface GetMimeTypeService {

	String getDetectedType(String filename) throws GetMimeTypeServiceException;

}
