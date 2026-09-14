package mx.egd.fmre.register.mapper.to_entity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.Aficionado;
import mx.egd.fmre.register.persistence.entity.AficionadoEntity;

@Mapper
public interface AficionadoMapper {
    AficionadoMapper INSTANCE = Mappers.getMapper(AficionadoMapper.class);

    @Mapping(source = "idPersona", target = "persona.idPersona")
    @Mapping(source = "idImagen", target = "imagen.idImagen")
    AficionadoEntity map(Aficionado aficionado);

}
