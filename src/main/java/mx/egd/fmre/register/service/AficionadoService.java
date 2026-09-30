package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.Aficionado;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.service.exceptions.AficionadoServiceException;

public interface AficionadoService {

    Aficionado save(Aficionado aficionado) throws AficionadoServiceException;

    Aficionado findByIdAficionado(Integer idAficionado) throws AficionadoServiceException;

    List<Aficionado> findByPersona(Persona persona) throws AficionadoServiceException;

    List<Aficionado> findActiveByPersona(Persona persona) throws AficionadoServiceException;

    Aficionado delete(Integer idAficionado) throws AficionadoServiceException;

}
