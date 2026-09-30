package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.dto.Estado;

public interface EstadoService {

    List<Estado> getEstadoList();

    Estado getEstadoByIdEstado(Integer idEstado);

}
