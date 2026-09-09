package mx.egd.fmre.register.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import mx.egd.fmre.register.dto.Estado;
import mx.egd.fmre.register.persistence.entity.EstadoEntity;

@Mapper
public interface EstadoMapper {
    EstadoMapper INSTANCE = Mappers.getMapper(EstadoMapper.class);

    Estado map(EstadoEntity estado);
}
