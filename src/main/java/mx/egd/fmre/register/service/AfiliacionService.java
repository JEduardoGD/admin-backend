package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.dto.Persona;
import mx.egd.fmre.register.service.exceptions.AfiliacionServiceException;

public interface AfiliacionService {

    Afiliacion save(Afiliacion afiliacion) throws AfiliacionServiceException;

    Afiliacion findByIdAfiliacion(Integer idAfiliacion) throws AfiliacionServiceException;

    List<Afiliacion> findByPersona(Persona persona) throws AfiliacionServiceException;

}
