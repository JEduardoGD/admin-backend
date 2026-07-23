package mx.egd.fmre.register.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Domicilio;
import mx.egd.fmre.register.mapper.to_dto.DomicilioMapper;
import mx.egd.fmre.register.mapper.to_entity.DomicilioEntityMapper;
import mx.egd.fmre.register.persistence.entity.DomicilioEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.DomicilioRepository;
import mx.egd.fmre.register.service.DomicilioService;

@Service
@RequiredArgsConstructor
public class DomicilioServiceImpl implements DomicilioService {
    
    private final DomicilioRepository domicilioRepository;
    
    @Override
    public Domicilio saveNew(Domicilio domicilio) {
        DomicilioEntity domicilioEntity = DomicilioEntityMapper.INSTANCE.domicilioToDomicilioEntity(domicilio);
        DomicilioEntity savedDomicilioEntity = domicilioRepository.save(domicilioEntity);
        return DomicilioMapper.INSTANCE.domicilioEntityToDomicilio(savedDomicilioEntity);
    }

    @Override
    public  List<Domicilio> findByIdPersona(int idPersona) {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setIdPersona(idPersona);
        List<DomicilioEntity> domicilioEntityList = domicilioRepository.findByPersonaEntity(personaEntity);
        return domicilioEntityList.stream()
                .map(d -> DomicilioMapper.INSTANCE.domicilioEntityToDomicilio(d))
                .collect(Collectors.toList());
    }
}
