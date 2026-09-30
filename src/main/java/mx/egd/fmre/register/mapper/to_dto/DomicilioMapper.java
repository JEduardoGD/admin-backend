package mx.egd.fmre.register.mapper.to_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.Domicilio;
import mx.egd.fmre.register.persistence.entity.DomicilioEntity;

@Mapper
public interface DomicilioMapper {
    DomicilioMapper INSTANCE = Mappers.getMapper(DomicilioMapper.class);
    
    @Mapping(source="persona.idPersona", target = "idPersona")
    Domicilio domicilioEntityToDomicilio(DomicilioEntity domicilioEntity);
}
