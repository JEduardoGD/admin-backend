package mx.egd.fmre.register.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.TipoAfiliacion;
import mx.egd.fmre.register.persistence.entity.TipoAfiliacionEntity;

@Mapper
public interface TipoAfiliacionEntityMapper {
    TipoAfiliacionEntityMapper INSTANCE = Mappers.getMapper(TipoAfiliacionEntityMapper.class);

    TipoAfiliacion map(TipoAfiliacionEntity tipoAfiliacionEntity);

}
