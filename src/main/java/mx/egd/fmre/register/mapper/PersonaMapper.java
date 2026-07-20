package mx.egd.fmre.register.mapper;

import java.util.List;
import java.util.stream.Collectors;

import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;

public abstract class PersonaMapper {
    public static List<Persona> map(List<PersonaEntity> personaEntityList) {
        if (personaEntityList == null) {
            return null;
        }
        return personaEntityList.stream()
                .map(p -> map(p))
                .collect(Collectors.toList());
    }

    public static Persona map(PersonaEntity personaEntityList) {
        if (personaEntityList == null) {
            return null;
        }
        Persona persona = new Persona();
        persona.setIdPersona(personaEntityList.getIdPersona());
        persona.setNombre(personaEntityList.getNombre());
        persona.setPrimerApellido(personaEntityList.getPrimerApellido());
        persona.setSegundoApellido(personaEntityList.getSegundoApellido());
        persona.setFecNac(personaEntityList.getFecNac());
        return persona;
    }
    
    public static PersonaEntity map(Persona persona) {
        if (persona == null) {
            return null;
        }
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setIdPersona(persona.getIdPersona() != null ? persona.getIdPersona() : null);
        personaEntity.setNombre(persona.getNombre());
        personaEntity.setPrimerApellido(persona.getPrimerApellido());
        personaEntity.setSegundoApellido(persona.getSegundoApellido());
        personaEntity.setFecNac(persona.getFecNac() != null ? persona.getFecNac() : null);
        return personaEntity;
    }
}
