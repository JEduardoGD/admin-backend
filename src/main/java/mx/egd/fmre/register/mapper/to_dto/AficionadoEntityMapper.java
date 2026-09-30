package mx.egd.fmre.register.mapper.to_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.Aficionado;
import mx.egd.fmre.register.persistence.entity.AficionadoEntity;

@Mapper
public interface AficionadoEntityMapper {
    AficionadoEntityMapper INSTANCE = Mappers.getMapper(AficionadoEntityMapper.class);

    @Mapping(source = "persona.idPersona", target = "idPersona")
    @Mapping(source = "imagen.idImagen", target = "idImagen")
    Aficionado map(AficionadoEntity aficionadoEntity);

}
