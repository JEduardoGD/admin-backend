package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.Persona;

public interface PersonaService {
    List<Persona> specialSearchCriterionForTheExistenceOfPersons(Persona persona);

    Persona createNewPersona(Persona persona);

    Persona updatePersona(Persona persona);

    Persona findByIdPersona(int idPersona);
}
