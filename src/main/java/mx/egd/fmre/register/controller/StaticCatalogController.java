package mx.egd.fmre.register.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Estado;
import mx.egd.fmre.register.persistence.entity.TipoAfiliacionEntity;
import mx.egd.fmre.register.persistence.repository.TipoAfiliacionRepository;
import mx.egd.fmre.register.record.TipoImagen;
import mx.egd.fmre.register.service.EstadoService;
import mx.egd.fmre.register.service.TipoImagenService;
import mx.egd.fmre.register.util.StaticValues;

@RestController
@RequestMapping("static_catalog")
@RequiredArgsConstructor
public class StaticCatalogController {
    
	private final TipoImagenService tipoImagenService;
	private final EstadoService estadoService;
	private final TipoAfiliacionRepository tipoAfiliacionRepository;
	
	@GetMapping("tipo_imagen")
    public ResponseEntity<List<TipoImagen>> listAll(){
		List<TipoImagen> tipoImagenList = tipoImagenService.findAllActive();
        return new ResponseEntity<>(tipoImagenList, HttpStatus.OK);
	}
	
    @GetMapping("tipo_imagen/for_persona")
    public ResponseEntity<List<TipoImagen>> forPersona(){
        List<TipoImagen> tipoImagenList = tipoImagenService.getImageTypeForGroup(StaticValues.FOR_PERSONA);
        return new ResponseEntity<>(tipoImagenList, HttpStatus.OK);
    }
    
    @GetMapping("tipo_imagen/for_afiliacion")
    public ResponseEntity<List<TipoImagen>> forAfiliacion(){
        List<TipoImagen> tipoImagenList = tipoImagenService.getImageTypeForGroup(StaticValues.FOR_AFILIACION);
        return new ResponseEntity<>(tipoImagenList, HttpStatus.OK);
    }
    
    @GetMapping("estado")
    public ResponseEntity<List<Estado>> estado(){
        List<Estado> estadoList = estadoService.getEstadoList();
        return new ResponseEntity<>(estadoList, HttpStatus.OK);
    }
    
    @GetMapping("estado/{idEstado}")
    public ResponseEntity<Estado> byIdEstado(Integer idEstado){
        Estado estadoList = estadoService.getEstadoByIdEstado(idEstado);
        return new ResponseEntity<>(estadoList, HttpStatus.OK);
    }
    
    @GetMapping("tipo_afiliacion")
    public ResponseEntity<List<TipoAfiliacionEntity>> tipoAfiliacion(){
        List<TipoAfiliacionEntity> tipoAfiliacionList = tipoAfiliacionRepository.findAll();
        return new ResponseEntity<>(tipoAfiliacionList, HttpStatus.OK);
    }
}
