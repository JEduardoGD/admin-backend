package mx.egd.fmre.register.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.postalia.Localizacion;
import mx.egd.fmre.register.service.AddressService;

@RestController
@RequestMapping("address")
@RequiredArgsConstructor
public class AddressController {
	
	private final AddressService addressService;
	
    @GetMapping("by_cp/{cp}")
    public ResponseEntity<Localizacion> findById(@PathVariable String cp) {
    	Localizacion localizacion = addressService.byCodigoPostal(cp);
        return new ResponseEntity<>(localizacion, HttpStatus.CREATED);
    }
}