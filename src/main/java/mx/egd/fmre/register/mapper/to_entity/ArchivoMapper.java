package mx.egd.fmre.register.mapper.to_entity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.ArchivoDto;
import mx.egd.fmre.register.persistence.entity.ArchivoEntity;

@Mapper
public interface ArchivoMapper {

    ArchivoMapper INSTANCE = Mappers.getMapper(ArchivoMapper.class);

    @Mapping(source = "idTipoArchivo", target = "tipoArchivo.idTipoArchivo")
    @Mapping(source = "idPersona", target = "persona.idPersona")
    @Mapping(source = "idAfiliacion", target = "afiliacion.idAfiliacion")
    ArchivoEntity map(ArchivoDto archivoDto);
}
