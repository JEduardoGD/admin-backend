package mx.egd.fmre.register.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.service.PersonaService;


@RestController
@RequestMapping("/persona")
@RequiredArgsConstructor
public class PersonaController {
    
    private final PersonaService personaService;
    
    @PostMapping
    public ResponseEntity<Persona> createPerson(@RequestBody Persona persona) {
        Persona createdPersona = personaService.createNewPersona(persona);
        return new ResponseEntity<>(createdPersona, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Persona>> search(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String primerApellido,
            @RequestParam(required = false) String segundoApellido,
            @RequestParam(required = false) LocalDate fecnac) {
        Persona persona = new Persona();
        persona.setNombre(nombre != null ? nombre : null);
        persona.setPrimerApellido(primerApellido != null ? primerApellido : null);
        persona.setSegundoApellido(segundoApellido != null ? segundoApellido : null);
        persona.setFecNac(fecnac != null ? Date.from(fecnac.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null);
        List<Persona> personasList = personaService.specialSearchCriterionForTheExistenceOfPersons(persona);
        return new ResponseEntity<>(personasList, HttpStatus.OK);
    }
}
