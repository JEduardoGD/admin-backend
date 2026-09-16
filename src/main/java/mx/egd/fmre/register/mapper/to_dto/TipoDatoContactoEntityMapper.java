package mx.egd.fmre.register.mapper.to_dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.TipoDatoContacto;
import mx.egd.fmre.register.persistence.entity.TipoDatoContactoEntity;

@Mapper
public interface TipoDatoContactoEntityMapper {
    TipoDatoContactoEntityMapper INSTANCE = Mappers.getMapper(TipoDatoContactoEntityMapper.class);

    TipoDatoContacto map(TipoDatoContactoEntity tipoDatoContactoEntity);
}
