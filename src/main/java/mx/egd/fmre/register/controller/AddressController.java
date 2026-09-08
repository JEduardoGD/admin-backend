package mx.egd.fmre.register.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.dto.postalia.Localizacion;
import mx.egd.fmre.register.service.AddressService;
import mx.egd.fmre.register.service.exceptions.AddressServiceException;

@RestController
@RequestMapping("address")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @GetMapping("by_cp/{cp}")
    public ResponseEntity<Localizacion> findById(@PathVariable String cp) {
        try {
            return new ResponseEntity<>(addressService.byCodigoPostal(cp), HttpStatus.CREATED);
        } catch (AddressServiceException e) {
            log.error(e.getMessage());
            switch (e.getHttpErrorCode()) {
            case 401:
                return ResponseEntity.badRequest().build();
            case 404:
                return ResponseEntity.notFound().build();
            case 429:
            default:
                return ResponseEntity.internalServerError().build();
            }
        }
    }
}