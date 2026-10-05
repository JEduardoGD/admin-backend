package mx.egd.fmre.register.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.dto.ArchivoDto;
import mx.egd.fmre.register.service.ArchivoService;
import mx.egd.fmre.register.service.exceptions.ImagenServiceException;

@RestController
@RequestMapping("archivo")
@RequiredArgsConstructor
@Slf4j
public class ArchivoController {

    private final ArchivoService archivoService;


    @PostMapping
    public ResponseEntity<ArchivoDto> save(@RequestBody ArchivoDto archivoDto) {
        archivoDto.setIdArchivo(null);
        ArchivoDto savedImagen = archivoService.save(archivoDto);
        return new ResponseEntity<>(savedImagen, HttpStatus.CREATED);
    }

    @PostMapping("update")
    public ResponseEntity<ArchivoDto> update(@RequestBody ArchivoDto archivoDto) {
        if (archivoDto.getIdArchivo() == null) {
            return null;
        }
        ArchivoDto updatedImagen = archivoService.save(archivoDto);
        return new ResponseEntity<>(updatedImagen, HttpStatus.CREATED);
    }

    @GetMapping("find_by/idpersona/{idPersona}/idafiliacion/{idAfiliacion}")
    public ResponseEntity<List<ArchivoDto>> findByIdPersona(@PathVariable int idPersona, @PathVariable int idAfiliacion) {
        List<ArchivoDto> imagenList = archivoService.findByIdPersonaAndIdAfiliacion(idPersona, idAfiliacion);
        return new ResponseEntity<>(imagenList, HttpStatus.CREATED);
    }

    @GetMapping("find_by/id/{id}")
    public ResponseEntity<ArchivoDto> findById(@PathVariable int id) {
        ArchivoDto archivoDto = archivoService.findById(id);
        return new ResponseEntity<>(archivoDto, HttpStatus.OK);
    }

    @ExceptionHandler(ImagenServiceException.class)
    public ResponseEntity<byte[]> handleStorageFileNotFound(ImagenServiceException exc) {
        return ResponseEntity.notFound().build();
    }
}
