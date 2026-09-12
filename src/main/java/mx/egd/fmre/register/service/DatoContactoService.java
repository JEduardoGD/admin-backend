package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.DatoContacto;

public interface DatoContactoService {

    DatoContacto save(DatoContacto datoContacto);

    DatoContacto findById(int idDatoContacto);

    List<DatoContacto> findByIdPersona(int idPersona);

    DatoContacto delete(int idDatoContacto);
}
