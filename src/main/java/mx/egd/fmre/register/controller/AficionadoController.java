package mx.egd.fmre.register.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Aficionado;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.service.AficionadoService;
import mx.egd.fmre.register.service.exceptions.AficionadoServiceException;

@RestController
@RequestMapping("aficionado")
@RequiredArgsConstructor
public class AficionadoController {

    private final AficionadoService aficionadoService;

    @PostMapping
    public ResponseEntity<Aficionado> save(@RequestBody Aficionado aficionado) throws AficionadoServiceException {
        aficionado.setIdAficionado(null);
        Aficionado savedAficionado = aficionadoService.save(aficionado);
        return new ResponseEntity<>(savedAficionado, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Aficionado> update(@RequestBody Aficionado aficionado) throws AficionadoServiceException {
        if (aficionado.getIdAficionado() == null || aficionado.getIdAficionado() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Aficionado savedAficionado = aficionadoService.save(aficionado);
        return new ResponseEntity<>(savedAficionado, HttpStatus.OK);
    }

    @GetMapping("find_by/id_aficionado/{idAficionado}")
    public ResponseEntity<Aficionado> findById(@PathVariable int idAficionado) throws AficionadoServiceException {
        Aficionado aficionado = aficionadoService.findByIdAficionado(idAficionado);
        return new ResponseEntity<>(aficionado, HttpStatus.OK);
    }

    @DeleteMapping("{idAficionado}")
    public ResponseEntity<Aficionado> delete(@PathVariable int idAficionado) throws AficionadoServiceException {
        if (idAficionado <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Aficionado aficionado = aficionadoService.delete(idAficionado);
        return new ResponseEntity<>(aficionado, HttpStatus.OK);
    }

    @GetMapping("find_by/id_persona/{idPersona}")
    public ResponseEntity<List<Aficionado>> findByIdPersona(@PathVariable int idPersona) throws AficionadoServiceException {
        if (idPersona <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Persona persona = new Persona();
        persona.setIdPersona(idPersona);
        List<Aficionado> aficionadoList = aficionadoService.findByPersona(persona);
        return new ResponseEntity<>(aficionadoList, HttpStatus.OK);
    }

    @ExceptionHandler(AficionadoServiceException.class)
    public ResponseEntity<String> handleUnexpected(AficionadoServiceException ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
