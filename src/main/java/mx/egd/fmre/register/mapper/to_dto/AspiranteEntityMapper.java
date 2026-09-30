package mx.egd.fmre.register.mapper.to_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.Aspirante;
import mx.egd.fmre.register.persistence.entity.AspiranteEntity;

@Mapper
public interface AspiranteEntityMapper {
    AspiranteEntityMapper INSTANCE = Mappers.getMapper(AspiranteEntityMapper.class);

    @Mapping(source = "persona.idPersona", target = "idPersona")
    @Mapping(source = "estado.idEstado", target = "idEstado")
    Aspirante map(AspiranteEntity aspiranteEntity);

}
