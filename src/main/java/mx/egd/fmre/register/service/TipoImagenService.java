package mx.egd.fmre.register.service;

import java.util.List;

import mx.egd.fmre.register.record.TipoImagen;

public interface TipoImagenService {

	List<TipoImagen> findAllActive();

}
