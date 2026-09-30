package mx.egd.fmre.register.mapper.to_entity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.DatoContacto;
import mx.egd.fmre.register.persistence.entity.DatoContactoEntity;

@Mapper
public interface DatoContactoEntityMapper {
    DatoContactoEntityMapper INSTANCE = Mappers.getMapper(DatoContactoEntityMapper.class);

    @Mapping(source = "idPersona", target = "persona.idPersona")
    @Mapping(source = "idTipoDatoContacto", target = "tipoDatoContacto.idTipoDatoContacto")
    DatoContactoEntity map(DatoContacto datoContacto);

    @Mapping(source = "persona.idPersona", target = "idPersona")
    @Mapping(source = "tipoDatoContacto.idTipoDatoContacto", target = "idTipoDatoContacto")
    DatoContacto map(DatoContactoEntity datoContactoEntity);
}
