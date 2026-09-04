package mx.egd.fmre.register.service;

import mx.egd.fmre.register.dto.postalia.Localizacion;

public interface AddressService {

	Localizacion byCodigoPostal(String codigoPostal);

}
