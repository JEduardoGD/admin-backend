package mx.egd.fmre.register.mapper.to_dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.persistence.entity.TipoImagenDocumentoEntity;
import mx.egd.fmre.register.record.TipoImagen;

@Mapper
public interface TipoImagenDocumentoMapper {
    TipoImagenDocumentoMapper INSTANCE = Mappers.getMapper(TipoImagenDocumentoMapper.class);
    
    @Mapping(source="idTipoImagenDocumento", target = "idTipoImagen")
    TipoImagen map(TipoImagenDocumentoEntity tipoImagenDocumentoEntity);

}
