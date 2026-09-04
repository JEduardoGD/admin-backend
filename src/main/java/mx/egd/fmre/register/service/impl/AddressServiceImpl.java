package mx.egd.fmre.register.service.impl;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.postalia.Localizacion;
import mx.egd.fmre.register.service.AddressService;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
	
	private final RestClient postaliaRestClient;
	
	@Override
	public Localizacion byCodigoPostal(String codigoPostal) {
	    return postaliaRestClient
	            .get()
	            .uri(String.format("/api/codigos-postales/%s", codigoPostal))
	            .accept(MediaType.APPLICATION_JSON)
	            .retrieve()
	            .body(Localizacion.class);
	}
}
