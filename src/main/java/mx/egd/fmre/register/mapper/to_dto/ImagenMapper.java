package mx.egd.fmre.register.mapper.to_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.ImagenDto;
import mx.egd.fmre.register.persistence.entity.ImagenEntity;

@Mapper
public interface ImagenMapper {
    ImagenMapper INSTANCE = Mappers.getMapper(ImagenMapper.class);

    @Mapping(source = "persona.idPersona", target = "idPersona")
    @Mapping(source = "tipoImagenDocumento.idTipoImagenDocumento", target = "idTipoImagenDocumento")
    ImagenDto imagenEntityToImagenDto(ImagenEntity imagenEntity);
}
