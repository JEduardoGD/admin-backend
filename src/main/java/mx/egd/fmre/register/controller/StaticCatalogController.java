package mx.egd.fmre.register.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.record.TipoImagen;
import mx.egd.fmre.register.service.TipoImagenService;

@RestController
@RequestMapping("static_catalog")
@RequiredArgsConstructor
public class StaticCatalogController {
	private final TipoImagenService tipoImagenService;
	@GetMapping("tipo_imagen")
    public ResponseEntity<List<TipoImagen>> listAll(){
		List<TipoImagen> tipoImagenList = tipoImagenService.findAllActive();
        return new ResponseEntity<>(tipoImagenList, HttpStatus.OK);
	}
}
