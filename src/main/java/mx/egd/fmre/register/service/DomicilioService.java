package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.Domicilio;

public interface DomicilioService {

    Domicilio saveNew(Domicilio domicilio);

    List<Domicilio> findByIdPersona(int idPersona);
}
