package mx.egd.fmre.register.mapper.to_entity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.ImagenDto;
import mx.egd.fmre.register.persistence.entity.ImagenEntity;

@Mapper
public interface ImagenEntityMapper {
    ImagenEntityMapper INSTANCE = Mappers.getMapper(ImagenEntityMapper.class);

    @Mapping(source = "idPersona", target = "persona.idPersona")
    @Mapping(source = "idTipoImagenDocumento", target = "tipoImagenDocumento.idTipoImagenDocumento")
    ImagenEntity imagenDtoToImagenEntity(ImagenDto imagenDto);
}
