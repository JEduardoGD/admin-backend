package mx.egd.fmre.register.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Aspirante;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.service.AspiranteService;
import mx.egd.fmre.register.service.exceptions.AspiranteServiceException;

@RestController
@RequestMapping("aspirante")
@RequiredArgsConstructor
public class AspiranteController {

    private final AspiranteService aspiranteService;

    @PostMapping
    public ResponseEntity<Aspirante> save(@RequestBody Aspirante aspirante) throws AspiranteServiceException {
        aspirante.setIdAspirante(null);
        Aspirante savedAspirante = aspiranteService.save(aspirante);
        return new ResponseEntity<>(savedAspirante, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Aspirante> update(@RequestBody Aspirante aspirante) throws AspiranteServiceException {
        if (aspirante.getIdAspirante() == null || aspirante.getIdAspirante() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Aspirante savedAspirante = aspiranteService.save(aspirante);
        return new ResponseEntity<>(savedAspirante, HttpStatus.OK);
    }

    @GetMapping("find_by/id_aspirante/{idAspirante}")
    public ResponseEntity<Aspirante> findById(@PathVariable int idAspirante) throws AspiranteServiceException {
        Aspirante aspirante = aspiranteService.findByIdAspirante(idAspirante);
        return new ResponseEntity<>(aspirante, HttpStatus.OK);
    }

    @GetMapping("find_by/id_persona/{idPersona}")
    public ResponseEntity<List<Aspirante>> findByIdPersona(@PathVariable int idPersona) throws AspiranteServiceException {
        if (idPersona <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Persona persona = new Persona();
        persona.setIdPersona(idPersona);
        List<Aspirante> aspiranteList = aspiranteService.findByPersona(persona);
        return new ResponseEntity<>(aspiranteList, HttpStatus.OK);
    }

    @ExceptionHandler(AspiranteServiceException.class)
    public ResponseEntity<String> handleUnexpected(AspiranteServiceException ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
