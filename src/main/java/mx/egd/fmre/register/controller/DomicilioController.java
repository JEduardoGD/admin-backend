package mx.egd.fmre.register.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Domicilio;
import mx.egd.fmre.register.service.DomicilioService;

@RestController
@RequestMapping("domicilio")
@RequiredArgsConstructor
public class DomicilioController {
    
    private static final String AUTHORIZATION_STR = "Authorization";

    private final DomicilioService domicilioService;

    @PostMapping
    public ResponseEntity<Domicilio> save(
            @RequestHeader(AUTHORIZATION_STR) String authorization,
            @RequestBody Domicilio domicilio) {
        domicilio.setIdDomicilio(null);
        Domicilio savedDomicilio = domicilioService.save(domicilio);
        return new ResponseEntity<>(savedDomicilio, HttpStatus.CREATED);
    }

    @PostMapping("update")
    public ResponseEntity<Domicilio> update(
            @RequestHeader(AUTHORIZATION_STR) String authorization,
            @RequestBody Domicilio domicilio) {
        if(domicilio.getIdDomicilio()== null) {
            return null;
        }
       Domicilio updatedDomicilio = domicilioService.save(domicilio);
        return new ResponseEntity<>(updatedDomicilio, HttpStatus.CREATED);
    }

    @GetMapping("find_by/idpersona/{idPersona}")
    public ResponseEntity<List<Domicilio>> findById(@PathVariable int idPersona) {
        List<Domicilio> domicilioList = domicilioService.findByIdPersona(idPersona);
        return new ResponseEntity<>(domicilioList, HttpStatus.CREATED);
    }
}
