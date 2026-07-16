package mx.egd.fmre.register.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.egd.fmre.register.dto.Persona;

@RestController
@RequestMapping("/persona")
public class PersonaController {
    @PostMapping
    public ResponseEntity<Persona>createPerson(@RequestBody Persona persona){
        persona.setId(1);
        return new ResponseEntity<>(persona, HttpStatus.CREATED);
    }
}
