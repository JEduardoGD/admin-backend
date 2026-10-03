package mx.egd.fmre.register.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.ArchivoDto;
import mx.egd.fmre.register.mapper.to_dto.ArchivoEntityMapper;
import mx.egd.fmre.register.mapper.to_entity.ArchivoMapper;
import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;
import mx.egd.fmre.register.persistence.entity.ArchivoEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.ArchivoRepository;
import mx.egd.fmre.register.service.ArchivoService;

@Service
@RequiredArgsConstructor
public class ArchivoServiceImpl implements ArchivoService {

    private final ArchivoRepository archivoRepository;

    @Override
    public ArchivoDto save(ArchivoDto archivoDto) {
        ArchivoEntity archivoEntity = ArchivoMapper.INSTANCE.map(archivoDto);
        archivoEntity.setIdArchivo(null);
        archivoEntity = archivoRepository.save(archivoEntity);

        return ArchivoEntityMapper.INSTANCE.map(archivoEntity);
    }

    @Override
    public List<ArchivoDto> findByIdPersonaAndIdAfiliacion(Integer idPersona, Integer idAfiliacion) {
        if (idPersona != null && idAfiliacion != null) {
            return null;
        }
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setIdPersona(idPersona);

        AfiliacionEntity afiliacionEntity = new AfiliacionEntity();
        afiliacionEntity.setIdAfiliacion(idAfiliacion);

        List<ArchivoEntity> archivoEntityList = archivoRepository.findByPersonaAndAfiliacion(personaEntity,
                afiliacionEntity);

        if (archivoEntityList != null && !archivoEntityList.isEmpty()) {
            return archivoEntityList.stream().map(ArchivoEntityMapper.INSTANCE::map).toList();
        }
        return null;
    }

    @Override
    public ArchivoDto findById(Integer idArchivo) {
        ArchivoEntity archivoEntity = archivoRepository.findById(idArchivo).orElse(null);
        if(archivoEntity != null) {
            return ArchivoEntityMapper.INSTANCE.map(archivoEntity);
        }
        return null;
    }
}
