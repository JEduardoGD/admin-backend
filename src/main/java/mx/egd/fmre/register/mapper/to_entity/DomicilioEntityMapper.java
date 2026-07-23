package mx.egd.fmre.register.mapper.to_entity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.Domicilio;
import mx.egd.fmre.register.persistence.entity.DomicilioEntity;

@Mapper
public interface DomicilioEntityMapper {
    DomicilioEntityMapper INSTANCE = Mappers.getMapper(DomicilioEntityMapper.class);

    @Mapping(source = "idPersona", target = "persona.idPersona")
    DomicilioEntity domicilioToDomicilioEntity(Domicilio domicilio);
}
