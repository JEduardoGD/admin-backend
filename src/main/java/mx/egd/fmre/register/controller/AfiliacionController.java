package mx.egd.fmre.register.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.service.AfiliacionService;

@RestController
@RequestMapping("afiliacion")
@RequiredArgsConstructor
public class AfiliacionController {
    
    private final AfiliacionService afiliacionService;

    @PostMapping
    public ResponseEntity<Afiliacion> save(@RequestBody Afiliacion afiliacion) {
        afiliacion.setIdAfiliacion(null);
        Afiliacion savedDomicilio = afiliacionService.save(afiliacion);
        return new ResponseEntity<>(savedDomicilio, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Afiliacion> update(@RequestBody Afiliacion afiliacion) {
        if (afiliacion.getIdAfiliacion() == null || afiliacion.getIdAfiliacion() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Afiliacion savedAfiliacion = afiliacionService.save(afiliacion);
        return new ResponseEntity<>(savedAfiliacion, HttpStatus.CREATED);
    }

    @GetMapping("find_by/id_afiliacion/{idAfiliacion}")
    public ResponseEntity<Afiliacion> findById(@PathVariable int idAfiliacion) {
        Afiliacion afiliacion = afiliacionService.findByIdAfiliacion(idAfiliacion);
        return new ResponseEntity<>(afiliacion, HttpStatus.CREATED);
    }

    @GetMapping("find_by/id_persona/{idPersona}")
    public ResponseEntity<List<Afiliacion>> findByIdPersona(@PathVariable int idPersona) {
        if (idPersona <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Persona persona = new Persona();
        persona.setIdPersona(idPersona);
        List<Afiliacion> afiliacionList = afiliacionService.findByPersona(persona);
        return new ResponseEntity<>(afiliacionList, HttpStatus.CREATED);
    }
}
