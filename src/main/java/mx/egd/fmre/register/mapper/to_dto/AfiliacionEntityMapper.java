package mx.egd.fmre.register.mapper.to_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;

@Mapper
public interface AfiliacionEntityMapper {
    AfiliacionEntityMapper INSTANCE = Mappers.getMapper(AfiliacionEntityMapper.class);
    
    @Mapping(source="persona.idPersona", target = "idPersona")
    Afiliacion map(AfiliacionEntity afiliacionEntity);

}
