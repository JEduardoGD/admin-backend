package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.Afiliacion;
import mx.egd.fmre.register.dto.Persona;

public interface AfiliacionService {

    Afiliacion save(Afiliacion afiliacion);
    
    Afiliacion findByIdAfiliacion(Integer idAfiliacion);

    List<Afiliacion> findByPersona(Persona persona);

}
