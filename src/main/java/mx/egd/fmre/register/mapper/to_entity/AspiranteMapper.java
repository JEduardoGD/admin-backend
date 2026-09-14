package mx.egd.fmre.register.mapper.to_entity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.Aspirante;
import mx.egd.fmre.register.persistence.entity.AspiranteEntity;

@Mapper
public interface AspiranteMapper {
    AspiranteMapper INSTANCE = Mappers.getMapper(AspiranteMapper.class);

    @Mapping(source = "idPersona", target = "persona.idPersona")
    @Mapping(source = "idEstado", target = "estado.idEstado")
    AspiranteEntity map(Aspirante aspirante);

}
