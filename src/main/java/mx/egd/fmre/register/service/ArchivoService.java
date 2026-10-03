package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.ArchivoDto;

public interface ArchivoService {

    ArchivoDto save(ArchivoDto imagenDto);

    List<ArchivoDto> findByIdPersonaAndIdAfiliacion(Integer idPersona, Integer idAfiliacion);

    ArchivoDto findById(Integer idArchivo);

}
