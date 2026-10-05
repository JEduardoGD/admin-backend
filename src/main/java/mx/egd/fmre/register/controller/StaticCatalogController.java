package mx.egd.fmre.register.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Estado;
import mx.egd.fmre.register.dto.TipoAfiliacion;
import mx.egd.fmre.register.dto.TipoArchivoDto;
import mx.egd.fmre.register.dto.TipoDatoContacto;
import mx.egd.fmre.register.record.TipoImagen;
import mx.egd.fmre.register.service.EstadoService;
import mx.egd.fmre.register.service.TipoAfiliacionService;
import mx.egd.fmre.register.service.TipoArchivoService;
import mx.egd.fmre.register.service.TipoDatoContactoService;
import mx.egd.fmre.register.service.TipoImagenService;
import mx.egd.fmre.register.util.StaticValues;

@RestController
@RequestMapping("static_catalog")
@RequiredArgsConstructor
public class StaticCatalogController {
    
    private final TipoImagenService tipoImagenService;
    private final EstadoService estadoService;
    private final TipoAfiliacionService tipoAfiliacionService;
    private final TipoDatoContactoService tipoDatoContactoService;
    private final TipoArchivoService tipoArchivoService;

    private static final String TIPO_IMAGEN = "tipo_imagen";
    private static final String ESTADO = "estado";

	@GetMapping(TIPO_IMAGEN)
    public ResponseEntity<List<TipoImagen>> listAll(){
		List<TipoImagen> tipoImagenList = tipoImagenService.findAllActive();
        return new ResponseEntity<>(tipoImagenList, HttpStatus.OK);
	}
	
    @GetMapping(TIPO_IMAGEN + "/for_persona")
    public ResponseEntity<List<TipoImagen>> forPersona(){
        List<TipoImagen> tipoImagenList = tipoImagenService.getImageTypeForGroup(StaticValues.FOR_PERSONA);
        return new ResponseEntity<>(tipoImagenList, HttpStatus.OK);
    }
    
    @GetMapping(TIPO_IMAGEN + "/for_afiliacion")
    public ResponseEntity<List<TipoImagen>> forAfiliacion(){
        List<TipoImagen> tipoImagenList = tipoImagenService.getImageTypeForGroup(StaticValues.FOR_AFILIACION);
        return new ResponseEntity<>(tipoImagenList, HttpStatus.OK);
    }
    
    @GetMapping(ESTADO)
    public ResponseEntity<List<Estado>> estado(){
        List<Estado> estadoList = estadoService.getEstadoList();
        return new ResponseEntity<>(estadoList, HttpStatus.OK);
    }
    
    @GetMapping(ESTADO + "/{idEstado}")
    public ResponseEntity<Estado> byIdEstado(Integer idEstado){
        Estado estadoList = estadoService.getEstadoByIdEstado(idEstado);
        return new ResponseEntity<>(estadoList, HttpStatus.OK);
    }
    
    @GetMapping("tipo_afiliacion")
    public ResponseEntity<List<TipoAfiliacion>> tipoAfiliacion(){
        List<TipoAfiliacion> tipoAfiliacionList = tipoAfiliacionService.findAll();
        return new ResponseEntity<>(tipoAfiliacionList, HttpStatus.OK);
    }
    
    @GetMapping("tipo_datocontacto")
    public ResponseEntity<List<TipoDatoContacto>> tipoDatoContacto(){
        List<TipoDatoContacto> tipoDatoContactoList = tipoDatoContactoService.getTipoDatoContacto();
        return new ResponseEntity<>(tipoDatoContactoList, HttpStatus.OK);
    }
    
    /*
    @GetMapping("start_migration")
    public ResponseEntity<Estado> startMigration() {
        migracionService.migrate();
        return new ResponseEntity<>(new Estado(), HttpStatus.OK);
    }
    */

    @GetMapping("tipo_archivo")
    public ResponseEntity<List<TipoArchivoDto>> tipoArchivo() {
        List<TipoArchivoDto> tipoArchivoDtoList = tipoArchivoService.findAll();
        return new ResponseEntity<>(tipoArchivoDtoList, HttpStatus.OK);
    }
}
