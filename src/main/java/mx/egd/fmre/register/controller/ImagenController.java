package mx.egd.fmre.register.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.ImagenDto;
import mx.egd.fmre.register.service.ImagenService;

@RestController
@RequestMapping("imagen")
@RequiredArgsConstructor
public class ImagenController {

    private final ImagenService imagenService;

    @PostMapping
    public ResponseEntity<ImagenDto> save(@RequestBody ImagenDto imagenDto) {
        imagenDto.setIdImagen(null);
        ImagenDto savedImagen = imagenService.save(imagenDto);
        return new ResponseEntity<>(savedImagen, HttpStatus.CREATED);
    }

    @PostMapping("update")
    public ResponseEntity<ImagenDto> update(@RequestBody ImagenDto imagenDto) {
        if (imagenDto.getIdImagen() == null) {
            return null;
        }
        ImagenDto updatedImagen = imagenService.save(imagenDto);
        return new ResponseEntity<>(updatedImagen, HttpStatus.CREATED);
    }

    @GetMapping("find_by/idpersona/{idPersona}")
    public ResponseEntity<List<ImagenDto>> findById(@PathVariable int idPersona) {
        List<ImagenDto> imagenList = imagenService.findByIdPersona(idPersona);
        return new ResponseEntity<>(imagenList, HttpStatus.CREATED);
    }
}
