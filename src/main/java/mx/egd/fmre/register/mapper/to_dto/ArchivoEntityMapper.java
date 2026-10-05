package mx.egd.fmre.register.mapper.to_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.ArchivoDto;
import mx.egd.fmre.register.persistence.entity.ArchivoEntity;

@Mapper
public interface ArchivoEntityMapper {

    ArchivoEntityMapper INSTANCE = Mappers.getMapper(ArchivoEntityMapper.class);

    @Mapping(source = "tipoArchivo.idTipoArchivo", target = "idTipoArchivo")
    @Mapping(source = "persona.idPersona", target = "idPersona")
    @Mapping(source = "afiliacion.idAfiliacion", target = "idAfiliacion")
    ArchivoDto map(ArchivoEntity archivoEntity);
}
