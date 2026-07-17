package mx.egd.fmre.register.controller;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.egd.fmre.register.dto.Persona;


@RestController
@RequestMapping("/persona")
public class PersonaController {
    @PostMapping
    public ResponseEntity<Persona> createPerson(@RequestBody Persona persona) {
        persona.setIdPersona(1);
        return new ResponseEntity<>(persona, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Persona> search(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String primerApellido,
            @RequestParam(required = false) String segundoApellido,
            @RequestParam(required = false) LocalDate fecnac) {
        Persona persona = new Persona(1,"a","b", "c", new Date());
        return new ResponseEntity<>(persona, HttpStatus.CREATED);
    }
}
