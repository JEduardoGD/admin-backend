package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.Aspirante;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.persistence.entity.EstadoEntity;
import mx.egd.fmre.register.service.exceptions.AspiranteServiceException;

public interface AspiranteService {

    Aspirante save(Aspirante aspirante) throws AspiranteServiceException;

    Aspirante findByIdAspirante(Integer idAspirante) throws AspiranteServiceException;

    List<Aspirante> findByPersona(Persona persona) throws AspiranteServiceException;

    Integer calculateNextEstadoContador(EstadoEntity estado);

}
