package mx.egd.fmre.register.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.mapper.PersonaMapper;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.PersonaRepository;
import mx.egd.fmre.register.service.PersonaService;
import mx.egd.fmre.register.util.DistinctByKey;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;

    @Override
    public List<Persona> specialSearchCriterionForTheExistenceOfPersons(Persona persona) {
        List<PersonaEntity> list = new ArrayList<>();
        if (persona == null) {
            return null;
        }
        //by Person ID
        if (persona.getIdPersona() != null) {
            PersonaEntity personaEntity = personaRepository
                    .findById(persona.getIdPersona()).orElse(null);
            if(personaEntity != null) {
                list.add(personaEntity);
            }
        }
        //By Nombre and Primer apellido
        if (persona.getNombre() != null && persona.getPrimerApellido() != null) {
            List<PersonaEntity> personasTemp = personaRepository
                    .findByNombreAndPrimerApellido(persona.getNombre(), persona.getPrimerApellido());
            if (personasTemp != null && personasTemp.size() > 0) {
                list.addAll(personasTemp);
            }
        }
        if (persona.getFecNac() != null) {
            List<PersonaEntity> personasTemp = personaRepository
                    .findByFecnac(persona.getFecNac());
            if (personasTemp != null && personasTemp.size() > 0) {
                list.addAll(personasTemp);
            }
        }
        List<PersonaEntity> listPersonaEntity = list.stream()
        .filter(new DistinctByKey<PersonaEntity>(PersonaEntity::getIdPersona)::filterByKey)
        .collect(Collectors.toList());
        return PersonaMapper.map(listPersonaEntity);
    }
    
    @Override
    public Persona createNewPersona(Persona persona) {
        if (persona == null) {
            return persona;
        }
        persona.setIdPersona(null);
        PersonaEntity personaEntity = PersonaMapper.map(persona);
        PersonaEntity savedPersonaEntity = personaRepository.save(personaEntity);
        return PersonaMapper.map(savedPersonaEntity);
    }
    
    @Override
    public Persona updatePersona(Persona persona) {
        if (persona == null) {
            return persona;
        }
        PersonaEntity personaEntity = PersonaMapper.map(persona);
        PersonaEntity savedPersonaEntity = personaRepository.save(personaEntity);
        return PersonaMapper.map(savedPersonaEntity);
    }
    
    @Override
    public Persona findByIdPersona(int idPersona) {
        PersonaEntity personaEntity = personaRepository.findById(idPersona).orElse(null);
        return PersonaMapper.map(personaEntity);
    }
}


