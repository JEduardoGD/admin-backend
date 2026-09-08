package mx.egd.fmre.register.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.component.AfiliacionValidatorComponent;
import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.mapper.to_dto.AfiliacionEntityMapper;
import mx.egd.fmre.register.mapper.to_entity.AfiliacionMapper;
import mx.egd.fmre.register.persistence.entity.AfiliacionEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.AfiliacionRepository;
import mx.egd.fmre.register.service.AfiliacionService;
import mx.egd.fmre.register.service.exceptions.AfiliacionServiceException;

@Service
@RequiredArgsConstructor
public class AfiliacionServiceImpl implements AfiliacionService   {
    
    private final AfiliacionRepository afiliacionRepository;
    private final AfiliacionValidatorComponent afiliacionValidatorComponent;
    
    @Override
    public Afiliacion save(Afiliacion afiliacion) throws AfiliacionServiceException {
        AfiliacionEntity afiliacionEntity = AfiliacionMapper.INSTANCE.map(afiliacion);
        String afiliacionErrorPeriodOnAfiliacion = afiliacionValidatorComponent.validatePeriodOnAfiliacion(afiliacion);
        if (afiliacionErrorPeriodOnAfiliacion != null) {
            throw new AfiliacionServiceException(afiliacionErrorPeriodOnAfiliacion);
        }
        String afiliacionErrorOverlaps = afiliacionValidatorComponent.validateOverlaps(afiliacion);
        if (afiliacionErrorOverlaps != null) {
            throw new AfiliacionServiceException(afiliacionErrorOverlaps);
        }
        String periodForEditingValidation = afiliacionValidatorComponent.validatePeriodForEditing(afiliacion);
        if (periodForEditingValidation != null) {
            throw new AfiliacionServiceException(periodForEditingValidation);
        }
        
        AfiliacionEntity savedAfiliacionEntity  =  afiliacionRepository.save(afiliacionEntity);
        Afiliacion savedAfiliacion = AfiliacionEntityMapper.INSTANCE.map(savedAfiliacionEntity);
        return savedAfiliacion;
    }

    @Override
    public Afiliacion findByIdAfiliacion(Integer idAfiliacion) throws AfiliacionServiceException {
        AfiliacionEntity afiliacionEntity = afiliacionRepository.findById(idAfiliacion).orElse(null);
        return AfiliacionEntityMapper.INSTANCE.map(afiliacionEntity);
    }

    @Override
    public List<Afiliacion> findByPersona(Persona persona) throws AfiliacionServiceException {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setIdPersona(persona.getIdPersona());
        List<AfiliacionEntity> afiliacionEntity = afiliacionRepository.findByPersona(personaEntity);
        return afiliacionEntity.stream().map(a-> AfiliacionEntityMapper.INSTANCE.map(a)).collect(Collectors.toList());
    }
}
