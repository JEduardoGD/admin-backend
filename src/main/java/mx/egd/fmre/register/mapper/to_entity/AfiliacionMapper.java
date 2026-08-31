package mx.egd.fmre.register.mapper.to_entity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;

@Mapper
public interface AfiliacionMapper {
    AfiliacionMapper INSTANCE = Mappers.getMapper(AfiliacionMapper.class);

    @Mapping(source = "idPersona", target = "persona.idPersona")
    AfiliacionEntity map(Afiliacion afiliacion);
}
