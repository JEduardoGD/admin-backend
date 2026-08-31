package mx.egd.fmre.register.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.mapper.to_dto.AfiliacionEntityMapper;
import mx.egd.fmre.register.mapper.to_entity.AfiliacionMapper;
import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.AfiliacionRepository;
import mx.egd.fmre.register.service.AfiliacionService;

@Service
@RequiredArgsConstructor
public class AfiliacionServiceImpl implements AfiliacionService {
    
    private final AfiliacionRepository afiliacionRepository;
    
    @Override
    public Afiliacion save(Afiliacion afiliacion) {
        AfiliacionEntity afiliacionEntity = AfiliacionMapper.INSTANCE.map(afiliacion);
        AfiliacionEntity savedAfiliacionEntity  =  afiliacionRepository.save(afiliacionEntity);
        Afiliacion savedAfiliacion = AfiliacionEntityMapper.INSTANCE.map(savedAfiliacionEntity);
        return savedAfiliacion;
    }

    @Override
    public Afiliacion findByIdAfiliacion(Integer idAfiliacion) {
        AfiliacionEntity afiliacionEntity = afiliacionRepository.findById(idAfiliacion).orElse(null);
        return AfiliacionEntityMapper.INSTANCE.map(afiliacionEntity);
    }

    @Override
    public List<Afiliacion> findByPersona(Persona persona) {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setIdPersona(persona.getIdPersona());
        List<AfiliacionEntity> afiliacionEntity = afiliacionRepository.findByPersona(personaEntity);
        return afiliacionEntity.stream().map(a-> AfiliacionEntityMapper.INSTANCE.map(a)).collect(Collectors.toList());
    }
}
