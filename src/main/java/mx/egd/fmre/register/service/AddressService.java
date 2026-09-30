package mx.egd.fmre.register.service;

import mx.egd.fmre.register.dto.postalia.Localizacion;
import mx.egd.fmre.register.service.exceptions.AddressServiceException;

public interface AddressService {

    Localizacion byCodigoPostal(String codigoPostal) throws AddressServiceException;


}
