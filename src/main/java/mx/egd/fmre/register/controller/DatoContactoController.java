package mx.egd.fmre.register.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.DatoContacto;
import mx.egd.fmre.register.service.DatoContactoService;

@RestController
@RequestMapping("datocontacto")
@RequiredArgsConstructor
public class DatoContactoController {

    private final DatoContactoService datoContactoService;

    @PostMapping
    public ResponseEntity<DatoContacto> save(@RequestBody DatoContacto datoContacto) {
        datoContacto.setIdDatoContacto(null);
        DatoContacto savedDatoContacto = datoContactoService.save(datoContacto);
        return new ResponseEntity<>(savedDatoContacto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<DatoContacto> update(@RequestBody DatoContacto datoContacto) {
        if (datoContacto.getIdDatoContacto() == null || datoContacto.getIdDatoContacto() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        DatoContacto updatedDatoContacto = datoContactoService.save(datoContacto);
        return new ResponseEntity<>(updatedDatoContacto, HttpStatus.CREATED);
    }

    @GetMapping("find_by/id/{idDatoContacto}")
    public ResponseEntity<DatoContacto> findById(@PathVariable int idDatoContacto) {
        DatoContacto datoContacto = datoContactoService.findById(idDatoContacto);
        if (datoContacto == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(datoContacto, HttpStatus.OK);
    }

    @GetMapping("find_by/idpersona/{idPersona}")
    public ResponseEntity<List<DatoContacto>> findByIdPersona(@PathVariable int idPersona) {
        if (idPersona <= 0) {
            return ResponseEntity.badRequest().build();
        }
        List<DatoContacto> datoContactoList = datoContactoService.findByIdPersona(idPersona);
        return new ResponseEntity<>(datoContactoList, HttpStatus.OK);
    }
    
    @DeleteMapping("{idDatoContacto}")
    public ResponseEntity<DatoContacto> deleteByIdDatoContacto(@PathVariable int idDatoContacto) {
        if (idDatoContacto <= 0) {
            return ResponseEntity.badRequest().build();
        }
        DatoContacto datoContacto = datoContactoService.delete(idDatoContacto);
        return new ResponseEntity<>(datoContacto, HttpStatus.OK);
    }
    
}
