package mx.egd.fmre.register.service.impl;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.postalia.Localizacion;
import mx.egd.fmre.register.service.AddressService;
import mx.egd.fmre.register.service.exceptions.AddressServiceException;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
	
	private final RestClient postaliaRestClient;
	
	@Override
	public Localizacion byCodigoPostal(String codigoPostal) throws AddressServiceException {
	    ResponseEntity<Localizacion> response = postaliaRestClient
	            .get()
	            .uri(String.format("/api/codigos-postales/%s", codigoPostal))
	            .accept(MediaType.APPLICATION_JSON)
	            .retrieve()
	            .toEntity(Localizacion.class);
        if (response.getStatusCode().value() == 404) {
            throw new AddressServiceException(404, String.format("CP %s not found", codigoPostal));
        }
	    if (response.getStatusCode().value() == 401) {
	        throw new AddressServiceException(401, "Invalid token or not found");
	    }
        if (response.getStatusCode().value() == 429) {
            throw new AddressServiceException(429, "Daily limit reach");
        }
	    return response.getBody();
	}
}
