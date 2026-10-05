package mx.egd.fmre.register.mapper.to_dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.TipoArchivoDto;
import mx.egd.fmre.register.persistence.entity.TipoArchivoEntity;

@Mapper
public interface TipoArchivoEntityMapper {

    TipoArchivoEntityMapper INSTANCE = Mappers.getMapper(TipoArchivoEntityMapper.class);

    TipoArchivoDto map(TipoArchivoEntity tipoArchivoEntity);

}
